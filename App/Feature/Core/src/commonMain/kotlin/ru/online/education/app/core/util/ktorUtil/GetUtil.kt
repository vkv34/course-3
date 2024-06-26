package ru.online.education.app.core.util.ktorUtil

import domain.NotificationManager
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

suspend inline fun <reified T> HttpClient.safeGet(
    path: String,
    notificationManager: NotificationManager
) = try {
    get(path).body<ApiResult<T>>()
} catch (e: Exception) {
    notificationManager.sendError(e.message?:"", e.stackTraceToString())
    ApiResult.Error(e.message ?: "", e)
}
suspend inline fun <reified T> HttpClient.safeDelete(
    path: String,
    notificationManager: NotificationManager
) = try {
    delete(path).body<ApiResult<T>>()
} catch (e: Exception) {
    notificationManager.sendError(e.message?:"", e.stackTraceToString())
    ApiResult.Error(e.message ?: "", e)
}