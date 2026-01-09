package fox.starter.engine.service.stepdeterminant

interface StepDeterminantFactory {
    fun getStepDeterminant(businessEntity: String): StepDeterminant<Any, Any, Any, Any>
}