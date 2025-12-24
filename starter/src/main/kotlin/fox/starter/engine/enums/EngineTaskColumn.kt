package fox.starter.engine.enums

import fox.starter.engine.util.ColumnName


enum class EngineTaskColumn(
    override val columnName: String
): ColumnName {
    ID("id"),
    INSERTED("inserted"),
    PROCESS_TIME("process_time"),
    BUSINESS_ENTITY("business_entity"),
    BUSINESS_ENTITY_ID("business_entity_id"),
    TRACE_ID("trace_id"),
    TO_TERMINATE("to_terminate"),
    PROCESS_STATUS("process_status"),
    LOCK_VERSION("lock_version"),
    LOCK_TIME("lock_time"),
    LOCK_TIME_TO("lock_time_to"),
    RETRY_COUNT("retry_count"),
    UPDATED("updated");
}