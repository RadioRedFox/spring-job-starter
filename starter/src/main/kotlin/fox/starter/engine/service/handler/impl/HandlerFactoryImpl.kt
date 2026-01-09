package fox.starter.engine.service.handler.impl

import fox.starter.engine.exception.EngineRunTimeException
import fox.starter.engine.service.handler.Handler
import fox.starter.engine.service.handler.HandlerFactory

class HandlerFactoryImpl(
    handlers: List<Handler<Any, Any, Any>>
): HandlerFactory{

    val handlersMap: Map<String, Map<Any, Handler<Any, Any, Any>>> = handlers.groupBy { it.businessEntity }
        .mapValues { entry -> entry.value.associateBy { it.step } }

    override fun getHandler(businessEntity: String, step: Any): Handler<Any, Any, Any> {
        return handlersMap[businessEntity]?.get(step)
            ?: throw EngineRunTimeException("Handler not found for businessEntity: $businessEntity, step: $step")
    }
}