package fox.starter.engine.dao

import fox.starter.engine.entity.EngineTask

interface EngineTaskDao {
    fun pollTask(): List<EngineTask>

    fun deleteTask(task: EngineTask)

    fun updateTaskWithLockVersionCheck(task: EngineTask)
}