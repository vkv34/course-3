package ru.online.education.app.core.util.coruotines

suspend fun <T, R> Iterable<T>.suspendMap(suspendTransform: suspend (T) -> R): List<R> {
    val result = mutableListOf<R>()
    for (item in this) {
        result.add(suspendTransform(item))
    }
    return result
}