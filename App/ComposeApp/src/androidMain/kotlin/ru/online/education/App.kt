package ru.online.education

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.online.education.app.feature.di.instalDi
import ru.online.education.app.feature.di.installAppDi

class App: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            installAppDi()
        }
    }
}