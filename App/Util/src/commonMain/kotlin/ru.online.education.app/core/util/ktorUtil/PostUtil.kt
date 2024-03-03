package ru.online.education.app.core.util.ktorUtil

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
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
    post(path) {
        contentType(ContentType.Application.Json)
        setBody(body)
        builder()
    }.run {
//        if (V is String){
//            ApiResult.Success<V>(bodyAsText())
//        }else{
//        }
        body<ApiResult<V>>()
        
    }
        
} catch (e: Exception) {
    ApiResult.Error<V>("Ошбика", e)
}

