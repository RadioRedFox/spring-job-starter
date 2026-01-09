package fox.starter.engine.service.taskprocessor.impl

import fox.starter.engine.dao.EngineTaskDao
import fox.starter.engine.entity.EngineTask
import fox.starter.engine.enums.BusinessTaskStatus
import fox.starter.engine.enums.ProcessStatus
import fox.starter.engine.exception.EngineRunTimeException
import fox.starter.engine.service.handler.HandlerFactory
import fox.starter.engine.service.stepdeterminant.StepDeterminantFactory
import fox.starter.engine.service.taskprocessor.TaskProcessor

class TaskProcessorImpl(
    private val stepDeterminantFactory: StepDeterminantFactory,
    private val handlerFactory: HandlerFactory,
    private val engineTaskDao: EngineTaskDao
): TaskProcessor {
    override fun process(task: EngineTask) {
        when (task.processStatus) {
            ProcessStatus.SCHEDULED -> processScheduledTask(task)
            ProcessStatus.WAITING -> {}
            ProcessStatus.PROCESSING -> TODO()
        }
    }

    private fun processScheduledTask(task: EngineTask) {
        val stepDeterminant = stepDeterminantFactory.getStepDeterminant(task.businessEntity)
        val entityId = stepDeterminant.parseId(task.businessEntityId)
        val (businessTaskStatus, businessObject, step) = stepDeterminant.getStep(entityId)

        when(businessTaskStatus) {
            BusinessTaskStatus.SCHEDULED -> {
                if (step == null) throw EngineRunTimeException("Step is null for scheduled task")
            }
            BusinessTaskStatus.WAITING -> {
                task.processStatus = ProcessStatus.WAITING
            }
            BusinessTaskStatus.CANCELLED -> deleteTask(task)
        }
        val handler = handlerFactory.getHandler(task.businessEntity, step)
        handler.handle()
    }

    private fun hand

    private fun deleteTask(task: EngineTask) {
        engineTaskDao.deleteTask(task.id)
    }

    private
}