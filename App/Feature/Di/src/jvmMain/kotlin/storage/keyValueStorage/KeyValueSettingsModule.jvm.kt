package storage.keyValueStorage

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.PropertiesSettings
import com.russhwolf.settings.Settings
import java.util.Properties
import java.util.prefs.Preferences

val preferences = Preferences.userRoot().node("onlineEducationProps")
actual fun getSettings(): Settings {
    return com.russhwolf.settings.PreferencesSettings(preferences)
}