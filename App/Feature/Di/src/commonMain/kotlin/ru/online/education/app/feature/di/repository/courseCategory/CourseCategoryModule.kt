package ru.online.education.app.feature.di.repository.courseCategory

import data.CourseCategoryRepositoryImpl
import org.koin.dsl.module
import ru.online.education.domain.repository.CourseCategoryRepository

val courseCategoryModule = module {
    factory<CourseCategoryRepository> {
        CourseCategoryRepositoryImpl(
            client = get(),
            notificationManager = get()
        )

    }
}