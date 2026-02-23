package fox.starter.engine.service.taskprocessor.impl

import fox.starter.engine.dao.EngineTaskDao
import fox.starter.engine.entity.EngineTask
import fox.starter.engine.enums.BusinessTaskStatus
import fox.starter.engine.enums.ProcessStatus
import fox.starter.engine.exception.EngineRunTimeException
import fox.starter.engine.service.handler.HandlerFactory
import fox.starter.engine.service.stepdeterminant.StepDeterminantFactory
import fox.starter.engine.service.taskprocessor.ScheduledStatusTaskProcessor

class ScheduledStatusTaskProcessorImpl(
    private val stepDeterminantFactory: StepDeterminantFactory,
    private val handlerFactory: HandlerFactory,
    private val engineTaskDao: EngineTaskDao
): ScheduledStatusTaskProcessor {
    override fun process(task: EngineTask) {
        try{
            startProcess(task)
        } catch (_: NoNowException) {

        }
        catch (_: Exception) {

        }
    }

    private fun startProcess(task: EngineTask) {
        val stepDeterminant = stepDeterminantFactory.getStepDeterminant(task.businessEntity)
        val entityId = stepDeterminant.parseId(task.businessEntityId)
        val (businessTaskStatus, businessObject, step) = stepDeterminant.getStep(entityId)

        when (businessTaskStatus) {
            BusinessTaskStatus.SCHEDULED -> {
                if (step == null) throw EngineRunTimeException("Step is null for scheduled task")
                if (businessObject == null) throw EngineRunTimeException("Business object is null for scheduled task")
            }

            BusinessTaskStatus.WAITING -> {
                task.processStatus = ProcessStatus.WAITING
                task.lockTime = null
                task.lockTimeTo = null
                engineTaskDao.updateTaskWithLockVersionCheck(task)
                return
            }

            BusinessTaskStatus.CANCELLED -> {
                engineTaskDao.deleteTask(task)
                return
            }
        }


    }



    private class NoNowException(message: String) : RuntimeException(message)
    private class FatalException(message: String) : RuntimeException(message)
}