package ru.online.education.app.core.util.ktorUtil

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*
import util.ApiResult

suspend inline fun <reified T> HttpResponse.asApiResult(): ApiResult<T> {
    return if (status.isSuccess()) {
        ApiResult.Success(body())
    } else {
        ApiResult.Error(body())
    }
}