package repository

import model.PublicationDto
import model.ListResponse
import repository.defaults.Repository
import util.ApiResult

interface PublicationRepository: Repository<PublicationDto, Int> {

    override val pageSize: Int
        get() = 3

    suspend fun getByCourseId(courseId: Int, page: Int): ApiResult<ListResponse<PublicationDto>>

    suspend fun getByPublicationOnCourseId(publicationOnCourseId: Int): ApiResult<PublicationDto>
}