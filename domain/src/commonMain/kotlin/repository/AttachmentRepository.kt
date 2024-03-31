package repository

import model.ListResponse
import model.PublicationAttachmentDto
import repository.defaults.Repository
import util.ApiResult

interface AttachmentRepository: Repository<PublicationAttachmentDto, Int> {

    suspend fun uploadFile(
        file: ByteArray,
        fileName: String,
        publicationId: Int,
        progressChanged: (sentBytes: Long, totalBytes: Long) -> Unit
    ): ApiResult<PublicationAttachmentDto>

    suspend fun getAttachments(publicationId: Int): ApiResult<ListResponse<PublicationAttachmentDto>>
}