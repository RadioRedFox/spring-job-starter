package fox.starter.engine.service.stepdeterminant

import fox.starter.engine.enums.BusinessTaskStatus

data class TaskDataFromStepDeterminant<BusinessObjectType, StepType>(
    val businessTaskStatus: BusinessTaskStatus,
    val businessObject: BusinessObjectType?,
    val step: StepType?
)