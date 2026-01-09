package fox.starter.engine.service.handler

import fox.starter.engine.enums.PreconditionStatus

interface Handler<StepType, BusinessObjectType, ResultType> {
    val businessEntity: String
    val step: StepType
    fun preCondition(businessObject: BusinessObjectType): PreconditionStatus
    fun handle(businessObject: BusinessObjectType): ResultType
    fun onError(businessObject: BusinessObjectType, exception: Exception): OnErrorResult<ResultType>
}