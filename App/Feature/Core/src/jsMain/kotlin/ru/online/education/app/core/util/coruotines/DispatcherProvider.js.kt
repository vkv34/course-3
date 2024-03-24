package ru.online.education.app.core.util.coruotines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual object DispatcherProvider {
    actual val IO: CoroutineDispatcher
        get() = Dispatchers.Default
    actual val Default: CoroutineDispatcher
        get() = Dispatchers.Default
    actual val Main: CoroutineDispatcher
        get() = Dispatchers.Main
    actual val Unconfined: CoroutineDispatcher
        get() = Dispatchers.Unconfined
}