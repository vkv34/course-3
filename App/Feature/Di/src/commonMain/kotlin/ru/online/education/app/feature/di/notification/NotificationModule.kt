package ru.online.education.app.feature.di.notification

import domain.InAppNotificationManager
import domain.NotificationManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module
import ru.online.education.app.core.util.coruotines.DispatcherProvider

val notificationModule = module {
    single<NotificationManager> { InAppNotificationManager(
        CoroutineScope(DispatcherProvider.IO + SupervisorJob())
    ) }
}