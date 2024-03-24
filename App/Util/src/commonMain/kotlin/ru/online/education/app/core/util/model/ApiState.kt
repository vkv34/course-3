package ru.online.education.app.core.util.model

sealed class ApiState<T> {

    class Default<T> : ApiState<T>()
    class Loading<T> : ApiState<T>()
    class Success<T>(
        val data: T
    ) : ApiState<T>()

    class Error<T>(
        val error: kotlin.Error
    ) : ApiState<T>()

}