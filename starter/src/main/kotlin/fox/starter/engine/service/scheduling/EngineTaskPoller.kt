package fox.starter.engine.service.scheduling

internal interface EngineTaskPoller {
    fun startPollingTask()
    suspend fun pollTask()

}