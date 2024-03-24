import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import repository.account.accountModule
import repository.course.courseModule
import ru.online.education.app.feature.di.ktorClientModule
import storage.keyValueStorage.keyValueStorageModule

fun KoinApplication.installAppDi() {
    modules(
        ktorClientModule,
        keyValueStorageModule,
        accountModule,
        courseModule
    )
}


fun instalDi() = startKoin{
    installAppDi()
}