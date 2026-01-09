package fox.starter.engine.service.stepdeterminant.impl

import fox.starter.engine.exception.EngineRunTimeException
import fox.starter.engine.service.stepdeterminant.StepDeterminant
import fox.starter.engine.service.stepdeterminant.StepDeterminantFactory

class StepDeterminantFactoryImpl(steDeterminants: List<StepDeterminant<Any, Any, Any, Any>>) : StepDeterminantFactory {
    private val stepDeterminantMap: Map<String, StepDeterminant<Any, Any, Any, Any>> =
        steDeterminants.associateBy { it.businessEntity }

    override fun getStepDeterminant(businessEntity: String): StepDeterminant<Any, Any, Any, Any> {
        return stepDeterminantMap[businessEntity]
            ?: throw EngineRunTimeException("No StepDeterminant found for business entity: $businessEntity")
    }
}