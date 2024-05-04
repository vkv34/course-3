package ru.online.education.app.feature.di

import ru.online.education.app.feature.di.notification.notificationModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import ru.online.education.app.feature.di.repository.account.accountModule
import ru.online.education.app.feature.di.repository.course.courseModule
import ru.online.education.app.feature.di.repository.course.publicationAttachmentModule
import ru.online.education.app.feature.di.repository.courseCategory.courseCategoryModule
import ru.online.education.app.feature.di.repository.user.usersModule
import ru.online.education.app.feature.di.keyValueStorage.keyValueStorageModule
import ru.online.education.app.feature.di.repository.userOnCourse.publicationAnswerModule
import ru.online.education.app.feature.di.repository.userOnCourse.userOnCourseModule

fun KoinApplication.installAppDi() {
    modules(
        ktorClientModule,
        notificationModule,
        keyValueStorageModule,
        accountModule,
        courseModule,
        usersModule,
        courseCategoryModule,
        publicationAttachmentModule,
        userOnCourseModule,
        publicationAnswerModule
    )
}


fun instalDi() = startKoin{
    installAppDi()
}