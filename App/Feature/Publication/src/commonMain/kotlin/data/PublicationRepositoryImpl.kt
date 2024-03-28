package data

import domain.NotificationManager
import io.ktor.client.*
import model.ListResponse
import model.PublicationDto
import repository.PublicationRepository
import ru.online.education.app.core.util.ktorUtil.safeGet
import ru.online.education.app.core.util.ktorUtil.safePostAsJson
import util.ApiResult

class PublicationRepositoryImpl(
    private val httpClient: HttpClient,
    private val notificationManager: NotificationManager,
) : PublicationRepository {
    override suspend fun getByCourseId(courseId: Int, page: Int): ApiResult<ListResponse<PublicationDto>> =
        httpClient.safeGet("course/$courseId/publication/$page", notificationManager)

    override suspend fun getAll(page: Int): ApiResult<ListResponse<PublicationDto>> {
        TODO("Not yet implemented")
    }

    override suspend fun getById(id: Int): ApiResult<PublicationDto> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteById(id: Int): ApiResult<PublicationDto> {
        TODO("Not yet implemented")
    }

    override suspend fun update(data: PublicationDto): ApiResult<PublicationDto?> {
        TODO("Not yet implemented")
    }

    override suspend fun add(data: PublicationDto): ApiResult<PublicationDto> = httpClient.safePostAsJson(
        path = "course/${data.courseId}/publication/new",
        body = data,
        notificationManager = notificationManager
    )
}