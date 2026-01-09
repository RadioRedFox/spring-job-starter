package fox.starter.engine.service.handler


data class HandlerResult<ResulType>(
    val result: ResulType,
    val asyncHandlerFlag: Boolean = false,
)