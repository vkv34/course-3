package repository

import model.CourseDto
import model.ListResponse
import repository.defaults.Repository
import util.ApiResult

interface CourseRepository: Repository<CourseDto, Int> {
    suspend fun findByName(name : String): CourseDto

    suspend fun filterByUser(page: Int, userId: Int = 0): ApiResult<ListResponse<CourseDto>>
}