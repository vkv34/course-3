package ru.online.education.app.core.util.coruotines

import kotlinx.coroutines.CoroutineDispatcher

actual object DispatcherProvider {
    actual val IO: CoroutineDispatcher
        get() = TODO("Not yet implemented")
    actual val Default: CoroutineDispatcher
        get() = TODO("Not yet implemented")
    actual val Main: CoroutineDispatcher
        get() = TODO("Not yet implemented")
    actual val Unconfined: CoroutineDispatcher
        get() = TODO("Not yet implemented")
}