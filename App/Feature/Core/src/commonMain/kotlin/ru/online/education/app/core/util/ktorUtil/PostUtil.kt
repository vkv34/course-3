package ru.online.education.app.core.util.ktorUtil

import domain.NotificationManager
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import util.ApiResult

suspend inline fun <reified T> HttpClient.postAsJson(
    path: String,
    body: T,
    builder: HttpRequestBuilder.() -> Unit = {}
) = post(path) {
    contentType(ContentType.Application.Json)
    setBody(body)
    builder()
}

suspend inline fun <reified T, reified V> HttpClient.safePostAsJson(
    path: String,
    body: T,
    builder: HttpRequestBuilder.() -> Unit = {}
) = try {
    val response = post(path) {
        contentType(ContentType.Application.Json)
        setBody(body)
        builder()
    }

    response.body<ApiResult<V>>()
} catch (e: Exception) {
    ApiResult.Error<V>("Ошбика ${e.message}", e)
}

suspend inline fun <reified T, reified V> HttpClient.safePostAsJson(
    path: String,
    body: T,
    notificationManager: NotificationManager,
    builder: HttpRequestBuilder.() -> Unit = {}
) = try {
    val response = post(path) {
        contentType(ContentType.Application.Json)
        setBody(body)
        builder()
    }

    response.body<ApiResult<V>>()
} catch (e: Exception) {
    notificationManager.sendError(e.message ?: "Ошбика ${e.message}", e.stackTraceToString())
    ApiResult.Error<V>("Ошбика ${e.message}", e)
}
suspend inline fun <reified T, reified V> HttpClient.safePutAsJson(
    path: String,
    body: T,
    notificationManager: NotificationManager,
    builder: HttpRequestBuilder.() -> Unit = {}
) = try {
    val response = put(path) {
        contentType(ContentType.Application.Json)
        setBody(body)
        builder()
    }

    response.body<ApiResult<V>>()
} catch (e: Exception) {
    notificationManager.sendError(e.message ?: "Ошбика ${e.message}", e.stackTraceToString())
    ApiResult.Error<V>("Ошбика ${e.message}", e)
}

suspend inline fun <reified T> HttpClient.safeDelete(
    path: String
): ApiResult<T> = try {
    val response = delete(path)
    response.body<ApiResult<T>>()
} catch (e: Exception) {
    ApiResult.Error("Ошбика ${e.message}", e)
}


