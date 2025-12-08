package fox.starter.engine.dao.impl

import fox.starter.engine.dao.EngineTaskDao
import fox.starter.engine.entity.EngineTask
import fox.starter.engine.enums.EngineTaskColumn
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class EngineTaskDaoImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
    private val schema: String
): EngineTaskDao {
    companion object {
        private val TABLE_NAME = "engine_task"
        private val SHORT_NAME_TABLE = "et"
        val ALL_COLUMNS = EngineTaskColumn.entries.joinToString(",\n") { "$SHORT_NAME_TABLE.${it.name.lowercase()}"}
    }

    private val FULL_TABLE_NAME = "$schema$TABLE_NAME"

    override fun pollTask(): List<EngineTask> {
        return emptyList()
    }


}