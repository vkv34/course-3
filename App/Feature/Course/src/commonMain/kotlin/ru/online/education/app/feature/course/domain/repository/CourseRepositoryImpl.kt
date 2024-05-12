package ru.online.education.app.feature.course.domain.repository

import domain.NotificationManager
import io.ktor.client.*
import ru.online.education.app.core.util.ktorUtil.safeGet
import ru.online.education.app.core.util.ktorUtil.safePostAsJson
import ru.online.education.app.core.util.ktorUtil.safePutAsJson
import ru.online.education.domain.repository.CourseRepository
import ru.online.education.domain.repository.model.BaseModel
import ru.online.education.domain.repository.model.CourseDto
import ru.online.education.domain.repository.model.ListResponse
import util.ApiResult

class CourseRepositoryImpl(
    private val client: HttpClient,
    private val notificationManager: NotificationManager
) : CourseRepository {
    override suspend fun findByName(name: String): CourseDto {
        TODO("Not yet implemented")
    }

    override suspend fun filterByUser(page: Int, userId: Int, archived: Boolean): ApiResult<ListResponse<CourseDto>> =
        client.safeGet("course/my/$page?archived=$archived", notificationManager = notificationManager)

    override suspend fun archiveCourse(courseId: Int, archived: Boolean): ApiResult<CourseDto> = client.safePostAsJson(
        path = "course/archive/$courseId?archived=$archived",
        body = ApiResult.Empty<BaseModel>(""),
        notificationManager = notificationManager
    )

    override suspend fun getAll(page: Int): ApiResult<ListResponse<CourseDto>> =
        client.safeGet("course/my/$page", notificationManager = notificationManager)

    override suspend fun getById(id: Int): ApiResult<CourseDto> = client.safeGet("course/$id")

    override suspend fun deleteById(id: Int): ApiResult<CourseDto> {
        TODO("Not yet implemented")
    }

    override suspend fun update(data: CourseDto): ApiResult<CourseDto?> =
        client.safePutAsJson(
            path = "course",
            body = data,
            notificationManager
        )

    override suspend fun add(data: CourseDto): ApiResult<CourseDto> =
        client.safePostAsJson("course/new", data, notificationManager)
}