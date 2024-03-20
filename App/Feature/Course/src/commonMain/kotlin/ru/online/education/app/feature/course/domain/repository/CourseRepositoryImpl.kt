package ru.online.education.app.feature.course.domain.repository

import io.ktor.client.*
import kotlinx.coroutines.CoroutineScope
import model.CourseDto
import model.ListResponse
import repository.CourseRepository
import ru.online.education.app.core.util.ktorUtil.safeGet
import ru.online.education.app.core.util.ktorUtil.safePostAsJson
import util.ApiResult

class CourseRepositoryImpl(
    val client: HttpClient
) : CourseRepository {
    override suspend fun findByName(name: String): CourseDto {
        TODO("Not yet implemented")
    }

    override suspend fun filterByUser(page: Int, userId: Int): ApiResult<ListResponse<CourseDto>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAll(page: Int): ApiResult<ListResponse<CourseDto>> = client.safeGet("course/my/$page")

    override suspend fun getById(id: Int): ApiResult<CourseDto> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteById(id: Int): ApiResult<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun update(data: CourseDto): ApiResult<CourseDto?> {
        TODO("Not yet implemented")
    }

    override suspend fun add(data: CourseDto): ApiResult<CourseDto> = client.safePostAsJson("course", data)
}