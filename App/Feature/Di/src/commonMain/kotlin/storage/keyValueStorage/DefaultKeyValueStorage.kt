package storage.keyValueStorage

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.toFlowSettings
import com.russhwolf.settings.coroutines.toSuspendSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import ru.online.education.app.core.util.coruotines.DispatcherProvider
import ru.online.education.app.core.util.store.KeyValueStorage

@OptIn(ExperimentalSettingsApi::class)
class DefaultKeyValueStorage(
    private val settings: Settings
) : KeyValueStorage<String, String> {

    val suspendSettings = settings.toSuspendSettings(DispatcherProvider.IO)
    val flowSettings = MutableStateFlow<Map<String, String?>>(
        mapOf()
    )

    override suspend fun store(key: String, value: String) {
        suspendSettings.putString(key, value)
        flowSettings.update { it.plus(key to value) }
    }


    override suspend fun read(key: String): String? {
        val value = suspendSettings.getStringOrNull(key)
        flowSettings.update { it.plus(key to value) }
        return value
    }

    override fun readAsFlow(key: String): Flow<String?> = flowSettings
        .map { it[key] }
        .distinctUntilChanged()

    override suspend fun remove(key: String) {
        suspendSettings.remove(key)
        flowSettings.update { it.minus(key) }
    }


}