package fox.starter.engine.service.handler

import fox.starter.engine.enums.ErrorTaskStatus

data class OnErrorResult<ResultType>(
    val errorTaskStatus: ErrorTaskStatus,
    val retryDelayMillis: Long = 0L,
    val errorResult: ResultType?
)