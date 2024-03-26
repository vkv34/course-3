package repository.account

import org.koin.dsl.module
import repository.AccountRepository
import ru.online.education.app.feature.account.domain.repository.AuthCallback
import ru.online.education.app.feature.account.domain.repository.AuthCallbackImpl
import ru.online.education.app.feature.account.domain.repository.UserAuthStore
import ru.online.education.app.feature.account.domain.repository.impl.AccountRepositoryImpl
import ru.online.education.app.feature.account.domain.repository.impl.UserAuthstoreImpl
import ru.online.education.app.feature.di.ktorClientModule

val accountModule = module(

) {
    includes(ktorClientModule)
    single<UserAuthStore> {
        UserAuthstoreImpl(
            keyValueStorage = get(),
            notificationManager = get()

        )
    }
    factory<AuthCallback> {
        AuthCallbackImpl(
            authstore = get()
        )
    }
    factory<AccountRepository> {
        AccountRepositoryImpl(
            client = get(),
            authCallback = get(),
            notificationManager = get()
        )
    }
}