package ru.online.education.app.feature.publication.data

import io.ktor.client.*
import ru.online.education.app.core.util.ktorUtil.safeDelete
import ru.online.education.domain.repository.model.ListResponse
import ru.online.education.domain.repository.model.PublicationOnCourseDto
import ru.online.education.domain.repository.PublicationOnCourseRepository
import util.ApiResult

class PublicationOnCourseRepositoryImpl(
    private val httpClient: HttpClient
): PublicationOnCourseRepository {
    override suspend fun getAll(page: Int): ApiResult<ListResponse<PublicationOnCourseDto>> {
        TODO("Not yet implemented")
    }

    override suspend fun getById(id: Int): ApiResult<PublicationOnCourseDto> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteById(id: Int): ApiResult<PublicationOnCourseDto> = httpClient.safeDelete("publicationOnCourse/$id")

    override suspend fun update(data: PublicationOnCourseDto): ApiResult<PublicationOnCourseDto?> {
        TODO("Not yet implemented")
    }

    override suspend fun add(data: PublicationOnCourseDto): ApiResult<PublicationOnCourseDto> {
        TODO("Not yet implemented")
    }
}