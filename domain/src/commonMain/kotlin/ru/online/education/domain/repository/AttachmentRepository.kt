package ru.online.education.domain.repository

import ru.online.education.domain.repository.model.ListResponse
import ru.online.education.domain.repository.model.PublicationAttachmentDto
import ru.online.education.domain.repository.defaults.Repository
import ru.online.education.domain.repository.model.PublicationAttachmentType
import util.ApiResult

interface AttachmentRepository: Repository<PublicationAttachmentDto, Int> {

    suspend fun uploadFile(
        file: ByteArray,
        fileName: String,
        publicationId: Int,
        progressChanged: (sentBytes: Long, totalBytes: Long) -> Unit,
        fileType: PublicationAttachmentType = PublicationAttachmentType.File
    ): ApiResult<PublicationAttachmentDto>

    suspend fun getAttachments(publicationId: Int): ApiResult<ListResponse<PublicationAttachmentDto>>
}