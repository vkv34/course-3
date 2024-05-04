package ru.online.education.app.feature.di.keyValueStorage

import com.russhwolf.settings.Settings
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.online.education.app.core.util.store.KeyValueStorage

expect class SettingsPreferences

expect fun getSettings(preferences: SettingsPreferences): Settings
expect val settingPreferencesModule: Module

val keyValueStorageModule = module {
    includes(settingPreferencesModule)
    single { getSettings(get()) }
    factory<KeyValueStorage<String, String>> { DefaultKeyValueStorage(get()) }
}