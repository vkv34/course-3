package data

import domain.NotificationManager
import io.ktor.client.*
import ru.online.education.domain.repository.model.CourseCategoryDto
import ru.online.education.domain.repository.model.ListResponse
import ru.online.education.domain.repository.CourseCategoryRepository
import ru.online.education.app.core.util.ktorUtil.safeGet
import ru.online.education.app.core.util.ktorUtil.safePostAsJson
import util.ApiResult

class CourseCategoryRepositoryImpl(
    val client: HttpClient,
    val notificationManager: NotificationManager
) : CourseCategoryRepository {
    override suspend fun findByName(name: String, page: Int): ApiResult<ListResponse<CourseCategoryDto>> =
        client.safeGet("courseCategory?search=$name&page=$page", notificationManager)

    override suspend fun getAll(page: Int): ApiResult<ListResponse<CourseCategoryDto>> = client.safeGet(
        "courseCategory/all/$page",
        notificationManager
    )

    override suspend fun getById(id: Int): ApiResult<CourseCategoryDto> =
        client.safeGet("courseCategory/$id", notificationManager)

    override suspend fun deleteById(id: Int): ApiResult<CourseCategoryDto> {
        TODO("Not yet implemented")
    }

    override suspend fun update(data: CourseCategoryDto): ApiResult<CourseCategoryDto?> {
        TODO("Not yet implemented")
    }

    override suspend fun add(data: CourseCategoryDto): ApiResult<CourseCategoryDto> =
        client.safePostAsJson("courseCategory/new", data, notificationManager)
}