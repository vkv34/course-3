package ru.online.education.app.core.util.ktorUtil

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import util.ApiResult

suspend inline fun <reified T> HttpClient.safeGet(
    path: String
) = try {
    get(path).body<ApiResult<T>>()
} catch (e: Exception) {
    ApiResult.Error(e.message ?: "", e)
}