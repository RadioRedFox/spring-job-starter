package fox.starter.engine.dao

import fox.starter.engine.entity.EngineTask

interface EngineTaskDao {
    fun pollTask(): List<EngineTask>

    fun deleteTask(taskId: Long)

    fun updateTaskWithLockVersionCheck(task: EngineTask)
}