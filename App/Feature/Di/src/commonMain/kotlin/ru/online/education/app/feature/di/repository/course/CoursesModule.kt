package ru.online.education.app.feature.di.repository.course

import org.koin.dsl.module
import ru.online.education.app.feature.course.domain.repository.CourseRepositoryImpl
import ru.online.education.app.feature.publication.data.PublicationOnCourseRepositoryImpl
import ru.online.education.app.feature.publication.data.PublicationRepositoryImpl
import ru.online.education.domain.repository.CourseRepository
import ru.online.education.domain.repository.PublicationOnCourseRepository
import ru.online.education.domain.repository.PublicationRepository

val courseModule = module {
    factory<CourseRepository> {
        CourseRepositoryImpl(
            client = get(),
            notificationManager = get()
        )
    }
    factory<PublicationRepository> {
        PublicationRepositoryImpl(
            httpClient = get(),
            notificationManager = get()
        )
    }

    factory<PublicationOnCourseRepository> {
        PublicationOnCourseRepositoryImpl(
            httpClient = get(),
        )
    }
}