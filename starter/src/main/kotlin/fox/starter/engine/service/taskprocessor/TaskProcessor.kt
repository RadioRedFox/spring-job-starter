package fox.starter.engine.service.taskprocessor

import fox.starter.engine.entity.EngineTask

interface TaskProcessor {
    fun process(task: EngineTask)

}