package fox.starter.engine.service.handler

interface HandlerFactory {
    fun getHandler(businessEntity: String, step: Any): Handler<Any, Any, Any>
}