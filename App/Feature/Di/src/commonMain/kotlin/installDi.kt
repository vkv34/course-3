import notification.notificationModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import repository.account.accountModule
import repository.course.courseModule
import repository.course.publicationAttachmentModule
import repository.courseCategory.courseCategoryModule
import repository.user.usersModule
import ru.online.education.app.feature.di.ktorClientModule
import storage.keyValueStorage.keyValueStorageModule

fun KoinApplication.installAppDi() {
    modules(
        ktorClientModule,
        notificationModule,
        keyValueStorageModule,
        accountModule,
        courseModule,
        usersModule,
        courseCategoryModule,
        publicationAttachmentModule
    )
}


fun instalDi() = startKoin{
    installAppDi()
}