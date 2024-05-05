package ru.online.education.app.feature.publication.data

import domain.NotificationManager
import io.ktor.client.*
import ru.online.education.app.core.util.ktorUtil.safeDelete
import ru.online.education.app.core.util.ktorUtil.safeGet
import ru.online.education.app.core.util.ktorUtil.safePostAsJson
import ru.online.education.app.core.util.ktorUtil.safePutAsJson
import ru.online.education.domain.repository.PublicationAnswerRepository
import ru.online.education.domain.repository.model.ListResponse
import ru.online.education.domain.repository.model.PublicationAnswerDto
import util.ApiResult

class PublicationAnswerRepositoryImpl(
    private val httpClient: HttpClient,
    private val notificationManager: NotificationManager
) : PublicationAnswerRepository {
    override suspend fun getAllByPublicationOnCourseId(
        publicationOnCourseId: Int,
        page: Int
    ): ApiResult<ListResponse<PublicationAnswerDto>> =
        httpClient.safeGet(
            path = "$PATH/byPublicationOnCourseId/$page?publicationOnCourseId=$publicationOnCourseId",
            notificationManager = notificationManager
        )

    override suspend fun getAllByPublicationOnCourseId(
        publicationOnCourseId: Int,
        userId: Int,
        page: Int
    ): ApiResult<ListResponse<PublicationAnswerDto>> = httpClient.safeGet(
        path = "$PATH/byPublicationOnCourseId/$page" +
                "?publicationOnCourseId=$publicationOnCourseId" +
                "&userId=$userId",
        notificationManager = notificationManager
    )

    override suspend fun sendMarkAndComment(
        mark: Byte?,
        comment: String?,
        answerId: Int
    ): ApiResult<PublicationAnswerDto> = httpClient.safePutAsJson(
        path = buildString {
            append("publicationAnswer/mark?")
            if (!comment.isNullOrBlank()) {
                append("comment=${comment}&")
            }
            if (mark != null) {
                append("mark=${mark}&")
            }
            append("answerId=${answerId}")
        },
        body = PublicationAnswerDto(),
        notificationManager = notificationManager
    )

    override suspend fun getAll(page: Int): ApiResult<ListResponse<PublicationAnswerDto>> {
        TODO("Not yet implemented")
    }

    override suspend fun getById(id: Int): ApiResult<PublicationAnswerDto> = httpClient
        .safeGet(
            path = "$PATH/$id",
            notificationManager = notificationManager
        )

    override suspend fun deleteById(id: Int): ApiResult<PublicationAnswerDto> = httpClient
        .safeDelete(
            path = "$PATH/$id",
            notificationManager = notificationManager
        )

    override suspend fun update(data: PublicationAnswerDto): ApiResult<PublicationAnswerDto?> =
        httpClient.safePutAsJson(
            path = PATH,
            body = data,
            notificationManager = notificationManager
        )

    override suspend fun add(data: PublicationAnswerDto): ApiResult<PublicationAnswerDto> =
        httpClient.safePostAsJson(
            path = PATH,
            body = data,
            notificationManager = notificationManager
        )

    companion object {
        const private val PATH = "publicationAnswer"
    }
}