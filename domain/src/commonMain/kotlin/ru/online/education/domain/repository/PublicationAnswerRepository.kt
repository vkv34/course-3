package ru.online.education.domain.repository

import ru.online.education.domain.repository.defaults.Repository
import ru.online.education.domain.repository.model.ListResponse
import ru.online.education.domain.repository.model.PublicationAnswerAttachmentDto
import ru.online.education.domain.repository.model.PublicationAnswerDto
import util.ApiResult

interface PublicationAnswerRepository : Repository<PublicationAnswerDto, Int> {

    suspend fun getAllByPublicationOnCourseId(publicationOnCourseId: Int, page: Int): ApiResult<ListResponse<PublicationAnswerDto>>

    suspend fun getAllByPublicationOnCourseId(
        publicationOnCourseId: Int,
        userId: Int,
        page: Int
    ): ApiResult<ListResponse<PublicationAnswerDto>>

    suspend fun sendMarkAndComment(
        mark: Byte?,
        comment: String?,
        answerId: Int
    ): ApiResult<PublicationAnswerDto>
}