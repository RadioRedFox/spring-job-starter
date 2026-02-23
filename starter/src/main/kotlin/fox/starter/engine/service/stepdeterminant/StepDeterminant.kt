package fox.starter.engine.service.stepdeterminant

interface StepDeterminant<IdType, BusinessObjectType, StepType, ResultType> {

    val businessEntity: String

    fun parseId(businessEntityId: String): IdType

    fun getStep(businessEntityId: IdType): TaskDataFromStepDeterminant<BusinessObjectType, StepType>

    fun calculateNextStep(businessObject: BusinessObjectType,
                          currentStep: StepType,
                          handlerResult: ResultType): StepType? {
        return currentStep
    }

    fun moveStep(businessObject: BusinessObjectType, nextStep: StepType) {

    }
}