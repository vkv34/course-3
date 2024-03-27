package ru.online.education.app.core.util.coruotines

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

suspend fun <T, R> Iterable<T>.suspendMap(suspendTransform: suspend (T) -> R): List<R> =
    coroutineScope {
        map { async { suspendTransform(it) } }.awaitAll()
    }/*{
    val result = mutableListOf<R>()
    for (item in this) {
        result.add(suspendTransform(item))
    }
    return result
}*/