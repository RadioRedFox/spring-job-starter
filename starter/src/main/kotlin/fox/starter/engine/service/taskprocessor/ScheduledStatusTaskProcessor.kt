package fox.starter.engine.service.taskprocessor

import fox.starter.engine.entity.EngineTask

interface ScheduledStatusTaskProcessor {
    fun process(task: EngineTask)
}