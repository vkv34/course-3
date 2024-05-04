package ru.online.education.app.feature.di.keyValueStorage

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.PropertiesSettings
import com.russhwolf.settings.Settings
import org.koin.core.module.Module
import org.koin.dsl.module
import java.util.Properties
import java.util.prefs.Preferences

val jvmPrefs = Preferences.userRoot().node("onlineEducationProps")
actual fun getSettings(preferences: SettingsPreferences): Settings {
    return com.russhwolf.settings.PreferencesSettings(jvmPrefs)
}

actual class SettingsPreferences

actual val settingPreferencesModule: Module
    get() = module {
        single { SettingsPreferences() }
    }