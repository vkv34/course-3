package repository.course

import data.PublicationRepositoryImpl
import org.koin.dsl.module
import repository.CourseRepository
import repository.PublicationRepository
import ru.online.education.app.feature.course.domain.repository.CourseRepositoryImpl

val courseModule = module {
    factory<CourseRepository> {
        CourseRepositoryImpl(
            client = get(),
            notificationManager = get()
        )
    }
    factory<PublicationRepository> { PublicationRepositoryImpl(httpClient = get(), notificationManager = get()) }

}