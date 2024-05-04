package ru.online.education.app.feature.publication.data

import domain.NotificationManager
import io.ktor.client.*
import ru.online.education.app.core.util.ktorUtil.safeDelete
import ru.online.education.app.core.util.ktorUtil.safeGet
import ru.online.education.app.core.util.ktorUtil.safePostAsJson
import ru.online.education.app.core.util.ktorUtil.safePutAsJson
import ru.online.education.domain.repository.UserOnCourseRepository
import ru.online.education.domain.repository.model.ListResponse
import ru.online.education.domain.repository.model.UserOnCourseDto
import util.ApiResult

class UserOnCourseRepositoryImpl(
    private val httpClient: HttpClient,
    private val notificationManager: NotificationManager
) : UserOnCourseRepository {
    override suspend fun getUsersOnCourseByCourseId(
        courseId: Int,
        page: Int
    ): ApiResult<ListResponse<UserOnCourseDto>> = httpClient.safeGet(
        "userOnCourse/$courseId?page=$page",
        notificationManager = notificationManager
    )

    override suspend fun getAll(page: Int): ApiResult<ListResponse<UserOnCourseDto>> {
        TODO("Not yet implemented")
    }

    override suspend fun getById(id: Int): ApiResult<UserOnCourseDto> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteById(id: Int): ApiResult<UserOnCourseDto> = httpClient.safeDelete(
        "userOnCourse/$id",
        notificationManager = notificationManager
    )

    override suspend fun update(data: UserOnCourseDto): ApiResult<UserOnCourseDto?> = httpClient.safePutAsJson(
        path = "userOnCourse",
        body = data,
        notificationManager = notificationManager
    )

    override suspend fun add(data: UserOnCourseDto): ApiResult<UserOnCourseDto> = httpClient.safePostAsJson(
        "userOnCourse",
        body = data,
        notificationManager = notificationManager
    )
}