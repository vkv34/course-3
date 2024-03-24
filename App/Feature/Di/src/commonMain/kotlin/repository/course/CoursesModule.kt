package repository.course

import org.koin.dsl.module
import repository.CourseRepository
import ru.online.education.app.feature.course.domain.repository.CourseRepositoryImpl
import ru.online.education.app.feature.di.ktorClientModule

val courseModule = module {
    factory<CourseRepository> {
        CourseRepositoryImpl(
            client = get()
        )
    }
}