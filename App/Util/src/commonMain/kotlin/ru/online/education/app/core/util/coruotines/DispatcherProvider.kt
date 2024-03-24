package ru.online.education.app.core.util.coruotines

import kotlinx.coroutines.CoroutineDispatcher

expect object DispatcherProvider {
    val IO: CoroutineDispatcher
    val Default: CoroutineDispatcher
    val Main: CoroutineDispatcher
    val Unconfined: CoroutineDispatcher
}