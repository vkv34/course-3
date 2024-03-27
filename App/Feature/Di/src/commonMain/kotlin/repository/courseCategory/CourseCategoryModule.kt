package repository.courseCategory

import data.CourseCategoryRepositoryImpl
import org.koin.dsl.module
import repository.CourseCategoryRepository

val courseCategoryModule = module {
    factory<CourseCategoryRepository> {
        CourseCategoryRepositoryImpl(
            client = get(),
            notificationManager = get()
        )

    }
}