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
        private val ALL_COLUMNS = EngineTaskColumn.entries.joinToString(",\n") { "$SHORT_NAME_TABLE.${it.columnName}" }

        private val POLL_TASK_SQL = """
            BEGIN;
            
            WITH rows_for_update AS (
            SELECT id
            FROM engine_task et 
            WHERE 1 = 1
    	        and process_status = '${ProcessStatus.SCHEDULED.name}'
    	        and process_time <= now()
            ORDER BY process_time 
            FOR UPDATE SKIP LOCKED
            LIMIT :batchSize
            )       
            
            UPDATE engine_task et
            SET
                process_status = '${ProcessStatus.PROCESSING.name}',
                lock_version = lock_version + 1,
                lock_time = now(),
                lock_time_to = :lockTimeTo,
                updated = now()
            FROM rows_for_update r
            WHERE et.id = r.id
            RETURNING $ALL_COLUMNS;
            
            COMMIT;
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

    fun resultSetToEngineTask(rs: ResultSet): EngineTask =
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