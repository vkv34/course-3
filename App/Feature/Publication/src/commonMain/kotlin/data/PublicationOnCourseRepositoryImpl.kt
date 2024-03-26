package data

import io.ktor.client.*
import model.ListResponse
import model.PublicationOnCourseDto
import repository.PublicationOnCourseRepository
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

    override suspend fun deleteById(id: Int): ApiResult<PublicationOnCourseDto> {
        TODO("Not yet implemented")
    }

    override suspend fun update(data: PublicationOnCourseDto): ApiResult<PublicationOnCourseDto?> {
        TODO("Not yet implemented")
    }

    override suspend fun add(data: PublicationOnCourseDto): ApiResult<PublicationOnCourseDto> {
        TODO("Not yet implemented")
    }
}