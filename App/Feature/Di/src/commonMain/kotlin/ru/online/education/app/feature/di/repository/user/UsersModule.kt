package ru.online.education.app.feature.di.repository.user

import data.UserRepositoryImpl
import org.koin.dsl.module
import ru.online.education.domain.repository.UserRepository

val usersModule = module {
    factory<UserRepository> {
        UserRepositoryImpl(
            client = get(),
            notificationManager = get()
        )
    }
}