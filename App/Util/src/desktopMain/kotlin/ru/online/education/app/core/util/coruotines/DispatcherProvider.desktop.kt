package ru.online.education.app.core.util.coruotines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.Dispatchers

actual object DispatcherProvider {
    actual val IO: CoroutineDispatcher
        get() = newFixedThreadPoolContext(200, "IO")
    actual val Default: CoroutineDispatcher
        get() = Dispatchers.Default
    actual val Main: CoroutineDispatcher
        get() = TODO("Not yet implemented")
    actual val Unconfined: CoroutineDispatcher
        get() = TODO("Not yet implemented")
}