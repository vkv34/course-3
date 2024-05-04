package ru.online.education.app.feature.di.repository.userOnCourse

import org.koin.dsl.module
import ru.online.education.app.feature.publication.data.PublicationAnswerRepositoryImpl
import ru.online.education.app.feature.publication.data.PublicationRepositoryImpl
import ru.online.education.domain.repository.PublicationAnswerRepository
import ru.online.education.domain.repository.PublicationRepository

val publicationAnswerModule = module {
    single<PublicationAnswerRepository> {
        PublicationAnswerRepositoryImpl(
            httpClient = get(),
            notificationManager = get()
        )
    }
}