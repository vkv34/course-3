package ru.online.education.app.feature.account.domain.repository.impl

import domain.NotificationManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.online.education.app.core.util.coruotines.DispatcherProvider
import ru.online.education.app.core.util.store.KeyValueStorage
import ru.online.education.app.feature.account.domain.model.UserAuthData
import ru.online.education.app.feature.account.domain.repository.UserAuthStore

class UserAuthstoreImpl(
    private val keyValueStorage: KeyValueStorage<String, String>,
    private val notificationManager: NotificationManager
) : UserAuthStore {

    private val json = Json
    override suspend fun store(userAuthData: UserAuthData) {
        try {
            keyValueStorage.store(KEY, json.encodeToString(userAuthData))
        } catch (e: Exception) {
            notificationManager.sendError(
                e.message ?: "Ошибка при сохранении данных авторизаци",
                e.stackTraceToString()
            )
        }
    }

    override suspend fun logOut() {
        try {
            keyValueStorage.remove(KEY)
        } catch (e: Exception) {
            notificationManager.sendError(e.message ?: "Ошибка при деавторизации", e.stackTraceToString())
        }
    }

    override suspend fun read(): UserAuthData? {
        val userJson = keyValueStorage.read(KEY)
        return userAuthDataFromJson(userJson)
    }


    override fun readAsFlow(): SharedFlow<UserAuthData?> =
        keyValueStorage
            .readAsFlow(KEY)
            .map(::userAuthDataFromJson)
            .shareIn(CoroutineScope(SupervisorJob()+ DispatcherProvider.IO), started = SharingStarted.Eagerly)

    companion object {
        const val KEY = "AuthData"
    }

    private fun userAuthDataFromJson(jsonString: String?): UserAuthData? = if (jsonString.isNullOrBlank()) {
        null
    } else {
        json.decodeFromString(jsonString)
    }
}