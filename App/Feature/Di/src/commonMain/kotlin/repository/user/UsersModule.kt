package repository.user

import data.UserRepositoryImpl
import org.koin.dsl.module
import repository.UserRepository

val usersModule = module {
    factory<UserRepository> {
        UserRepositoryImpl(
            client = get(),
            notificationManager = get()
        )
    }
}