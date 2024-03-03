package ru.online.education.app.core.util.api

import ru.online.education.app.core.util.model.ApiState
import util.ApiResult

inline fun <reified T> ApiResult<T>.toApiState(): ApiState<T> = when (this) {
    is ApiResult.Empty -> ApiState.Default()
    is ApiResult.Error -> ApiState.Error(Error())
    is ApiResult.Loading -> ApiState.Loading()
    is ApiResult.Success -> ApiState.Success(data)
}
inline fun <reified T, reified V> ApiResult<T>.toApiState(mapper: (T) -> V): ApiState<V> = when (this) {
    is ApiResult.Empty -> ApiState.Default()
    is ApiResult.Error -> ApiState.Error(Error())
    is ApiResult.Loading -> ApiState.Loading()
    is ApiResult.Success -> ApiState.Success(mapper(data))
}