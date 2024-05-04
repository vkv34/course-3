package ru.online.education.app.feature.di.keyValueStorage

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.StorageSettings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toSuspendSettings
import org.koin.core.module.Module
import org.koin.dsl.module

val settings = StorageSettings()
actual fun getSettings(preferences: SettingsPreferences): Settings {
    return com.russhwolf.settings.StorageSettings()
}

actual class SettingsPreferences

actual val settingPreferencesModule: Module
    get() = module {
        single { SettingsPreferences() }
    }