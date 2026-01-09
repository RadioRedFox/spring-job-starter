package fox.starter.engine.dao.impl

import fox.starter.engine.dao.EngineTaskDao
import fox.starter.engine.entity.EngineTask
import fox.starter.engine.enums.EngineTaskColumn
import fox.starter.engine.enums.ProcessStatus
import fox.starter.engine.util.getBoolean
import fox.starter.engine.util.getInt
import fox.starter.engine.util.getLocalDateTime
import fox.starter.engine.util.getLong
import fox.starter.engine.util.getProcessStatus
import fox.starter.engine.util.getString
import fox.starter.engine.util.getUUID
import java.sql.ResultSet
import java.time.LocalDateTime
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class EngineTaskDaoImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : EngineTaskDao {
    companion object {
        private const val SHORT_NAME_TABLE = "et"
        private const val ENGINE_TASK_TABLE_NAME = "engine_task"
        private val ALL_COLUMNS = EngineTaskColumn.entries.joinToString(",\n") { "$SHORT_NAME_TABLE.${it.columnName}" }

        private val POLL_TASK_SQL = """
            BEGIN;
            
            WITH rows_for_update AS (
            SELECT ${EngineTaskColumn.ID.columnName}
            FROM $ENGINE_TASK_TABLE_NAME $SHORT_NAME_TABLE 
            WHERE 1 = 1
    	        and ${EngineTaskColumn.PROCESS_STATUS.columnName} = '${ProcessStatus.SCHEDULED.name}'
    	        and ${EngineTaskColumn.PROCESS_TIME.columnName} <= now()
            ORDER BY ${EngineTaskColumn.PROCESS_TIME.columnName} 
            FOR UPDATE SKIP LOCKED
            LIMIT :batchSize
            )       
            
            UPDATE $ENGINE_TASK_TABLE_NAME $SHORT_NAME_TABLE 
            SET
                ${EngineTaskColumn.PROCESS_STATUS.columnName} = '${ProcessStatus.PROCESSING.name}',
                ${EngineTaskColumn.LOCK_VERSION.columnName} = ${EngineTaskColumn.LOCK_VERSION.columnName} + 1,
                ${EngineTaskColumn.LOCK_TIME.columnName} = now(),
                ${EngineTaskColumn.LOCK_TIME_TO.columnName} = :lockTimeTo,
                ${EngineTaskColumn.UPDATED.columnName} = now()
            FROM rows_for_update r
            WHERE $SHORT_NAME_TABLE.${EngineTaskColumn.ID.columnName} = r.${EngineTaskColumn.ID.columnName}
            RETURNING $ALL_COLUMNS;
            
            COMMIT;
        """.trimIndent()

        private val DELETE_TASK = """
            delete from $ENGINE_TASK_TABLE_NAME where ${EngineTaskColumn.ID.columnName} = :id
        """.trimIndent()
    }

    override fun pollTask(): List<EngineTask> =
        namedParameterJdbcTemplate.query(
            POLL_TASK_SQL,
            mapOf(
                "batchSize" to 50,
                "lockTimeTo" to System.currentTimeMillis() + 5 * 60 * 1000 // 5 minutes
            )
        ) { rs, _ -> resultSetToEngineTask(rs) }

    override fun deleteTask(taskId: Long) {
        namedParameterJdbcTemplate.update(
            DELETE_TASK,
            mapOf("id" to taskId)
        )
    }

    override fun updateTaskWithLockVersionCheck(task: EngineTask) {
        val sqlRequest = StringBuilder("update $ENGINE_TASK_TABLE_NAME set \n")
        val requestProperties = mutableMapOf<String, Any?>()
        task.setSQLUpdateRequestAndChangedProperties(sqlRequest, requestProperties)
        sqlRequest.append("where ${EngineTaskColumn.ID.columnName} = :id \n")
        sqlRequest.append("and ${EngineTaskColumn.LOCK_VERSION.columnName} = :lockVersion")
        requestProperties["id"] = task.id
        requestProperties["lockVersion"] = task.lockVersion
        namedParameterJdbcTemplate.update(
            sqlRequest.toString(),
            requestProperties
        )
    }

    private fun resultSetToEngineTask(rs: ResultSet): EngineTask =
        EngineTask(
            id = rs.getLong(EngineTaskColumn.ID),
            inserted = rs.getLocalDateTime(EngineTaskColumn.INSERTED),
            processTime = rs.getLocalDateTime(EngineTaskColumn.PROCESS_TIME),
            businessEntity = rs.getString(EngineTaskColumn.BUSINESS_ENTITY),
            businessEntityId = rs.getString(EngineTaskColumn.BUSINESS_ENTITY_ID),
            traceId = rs.getUUID(EngineTaskColumn.TRACE_ID),
            toTerminate = rs.getBoolean(EngineTaskColumn.TO_TERMINATE),
            processStatus = rs.getProcessStatus(EngineTaskColumn.PROCESS_STATUS),
            lockVersion = rs.getLong(EngineTaskColumn.LOCK_VERSION),
            lockTime = rs.getLocalDateTime(EngineTaskColumn.LOCK_TIME),
            lockTimeTo = rs.getLocalDateTime(EngineTaskColumn.LOCK_TIME_TO),
            retryCount = rs.getInt(EngineTaskColumn.RETRY_COUNT),
            updated = rs.getLocalDateTime(EngineTaskColumn.UPDATED)
        )
}