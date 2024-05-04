package ru.online.education.app.feature.di.repository.userOnCourse

import org.koin.dsl.module
import ru.online.education.app.feature.publication.data.UserOnCourseRepositoryImpl
import ru.online.education.domain.repository.UserOnCourseRepository

val userOnCourseModule = module {
    single<UserOnCourseRepository> {
        UserOnCourseRepositoryImpl(
            httpClient = get(),
            notificationManager = get()
        )
    }
}