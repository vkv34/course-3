package ru.online.education.app.feature.di.repository.course

import ru.online.education.app.feature.publicationAttachment.repository.AttachmentRepositoryImpl
import org.koin.dsl.module
import ru.online.education.app.feature.publicationAttachment.repository.AnswerAttachmentRepositoryImpl
import ru.online.education.domain.repository.AnswerAttachmentRepository
import ru.online.education.domain.repository.AttachmentRepository

val publicationAttachmentModule = module {
    factory<AttachmentRepository> {
        AttachmentRepositoryImpl(
            client = get(),
            notificationManager = get()
        )
    }
    factory<AnswerAttachmentRepository> {
        AnswerAttachmentRepositoryImpl(
            httpClient = get(),
            notificationManager = get()
        )
    }
}