package repository.course

import data.repository.AttachmentRepositoryImpl
import org.koin.dsl.module
import repository.AttachmentRepository

val publicationAttachmentModule = module {
    factory<AttachmentRepository> {
        AttachmentRepositoryImpl(
            client = get(),
            notificationManager = get()
        )
    }
}