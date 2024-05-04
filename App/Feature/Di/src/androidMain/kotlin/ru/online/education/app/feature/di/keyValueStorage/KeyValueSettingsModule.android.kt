package ru.online.education.app.feature.di.keyValueStorage

import android.content.Context
import android.content.SharedPreferences
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module


actual fun getSettings(preferences: SettingsPreferences): Settings = SharedPreferencesSettings(
    delegate = preferences.sharedPreferences
)

actual class SettingsPreferences(
    val sharedPreferences: SharedPreferences,
)

actual val settingPreferencesModule: Module
    get() = module {

        single {
            val context = androidContext()
            val sharedPreferences =
                context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
            SettingsPreferences(
                sharedPreferences = sharedPreferences
            )
        }
    }