package ru.online.education.app.feature.di.keyValueStorage

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import org.koin.core.module.Module

actual fun getSettings(preferences: SettingsPreferences): Settings {
    return TODO() 
}

actual class SettingsPreferences

actual val settingPreferencesModule: Module
    get() = TODO("Not yet implemented")