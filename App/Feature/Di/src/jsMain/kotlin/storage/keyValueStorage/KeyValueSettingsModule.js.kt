package storage.keyValueStorage 

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.StorageSettings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toSuspendSettings

val settings = StorageSettings()
actual fun getSettings(): Settings {
    return com.russhwolf.settings.StorageSettings()
}