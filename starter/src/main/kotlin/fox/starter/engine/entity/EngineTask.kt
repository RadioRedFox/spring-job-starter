package fox.starter.engine.entity

import fox.starter.engine.enums.EngineTaskColumn
import fox.starter.engine.enums.ProcessStatus
import fox.starter.engine.util.observable
import java.time.LocalDateTime
import java.util.UUID
import kotlin.reflect.KProperty1

class EngineTask(
    id: Long,
    inserted: LocalDateTime = LocalDateTime.now(),
    processTime: LocalDateTime = LocalDateTime.now(),
    businessEntity: String,
    businessEntityId: String,
    traceId: UUID,
    toTerminate: Boolean = false,
    processStatus: ProcessStatus = ProcessStatus.SCHEDULED,
    lockVersion: Long = 0,
    deleteLockVersion: Long = 0,
    lockTime: LocalDateTime? = null,
    lockTimeTo: LocalDateTime? = null,
    retryCount: Int = 0,
    updated: LocalDateTime = LocalDateTime.now()
) {
    private val changedProperties = mutableSetOf<KProperty1<EngineTask, Any?>>()

    val id: Long = id
    val inserted: LocalDateTime = inserted
    var processTime: LocalDateTime by observable(processTime, changedProperties)
    val businessEntity: String = businessEntity
    val businessEntityId: String = businessEntityId
    val traceId: UUID = traceId
    var toTerminate: Boolean by observable(toTerminate, changedProperties)
    var processStatus: ProcessStatus by observable(processStatus, changedProperties)
    var lockVersion: Long by observable(lockVersion, changedProperties)
    var lockTime: LocalDateTime? by observable(lockTime, changedProperties)
    var lockTimeTo: LocalDateTime? by observable(lockTimeTo, changedProperties)
    var retryCount: Int by observable(retryCount, changedProperties)
    var updated: LocalDateTime = updated

    companion object {
        private val propertyToColumnNameMap = mapOf(
            EngineTask::processTime to EngineTaskColumn.PROCESS_TIME,
            EngineTask::traceId to EngineTaskColumn.TRACE_ID,
            EngineTask::toTerminate to EngineTaskColumn.TO_TERMINATE,
            EngineTask::processStatus to EngineTaskColumn.PROCESS_STATUS,
            EngineTask::lockVersion to EngineTaskColumn.LOCK_VERSION,
            EngineTask::lockTime to EngineTaskColumn.LOCK_TIME,
            EngineTask::lockTimeTo to EngineTaskColumn.LOCK_TIME_TO,
            EngineTask::retryCount to EngineTaskColumn.RETRY_COUNT,
            EngineTask::updated to EngineTaskColumn.UPDATED,
        )
    }

    fun setSQLUpdateRequestAndChangedProperties(
        requestBuilder: StringBuilder,
        requestProperties: MutableMap<String, Any?>
    ) {
        changedProperties.forEach{
            property ->
            addParameter(requestBuilder, requestProperties, property)
            requestBuilder.append(",\n")
        }

        addParameter(requestBuilder, requestProperties, EngineTask::updated)
    }

    private fun addParameter(
        requestBuilder: StringBuilder,
        requestProperties: MutableMap<String, Any?>,
        property: KProperty1<EngineTask, Any?>
    ) {
        val columnName = propertyToColumnNameMap[property]!!.columnName
        requestBuilder.append("$columnName = :${property.name}")
        when(property) {
            EngineTask::processStatus -> requestProperties[property.name] = processStatus.name
            else -> requestProperties[property.name] = property.get(this)
        }
    }
}
