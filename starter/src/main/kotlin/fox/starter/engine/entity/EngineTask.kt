package fox.starter.engine.entity

import java.time.LocalDateTime
import java.util.UUID

data class EngineTask(
    val id: Long,
    val inserted: LocalDateTime = LocalDateTime.now(),
    var processTime: LocalDateTime = LocalDateTime.now(),
    val businessEntity: String,
    val businessKey: UUID,
    val traceId: UUID,
    val flow: String,
    val flowLevel: Int = 0,
    var step: String,
    var toTerminate: Boolean = false,
    var processStatus: String,
    var lockVersion: Long = 0,
    var lockTime: LocalDateTime? = null,
    var lockTimeTo: LocalDateTime? = null,
    var lockAsync: Boolean = false,
    var stepResultAsync: String? = null,
    var retryCount: Int = 0,
    var updated: LocalDateTime = LocalDateTime.now()
)
