package repository

import model.PublicationDto
import model.ListResponse
import repository.defaults.Repository
import util.ApiResult

interface PublicationRepository: Repository<PublicationDto, Int> {

    suspend fun getByCourseId(courseId: Int, page: Int): ApiResult<ListResponse<PublicationDto>>


}