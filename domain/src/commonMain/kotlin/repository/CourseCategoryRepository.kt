package repository

import model.CourseCategoryDto
import model.ListResponse
import repository.defaults.Repository
import util.ApiResult

interface CourseCategoryRepository: Repository<CourseCategoryDto, Int> {
    suspend fun findByName(name: String, page: Int): ApiResult<ListResponse<CourseCategoryDto>>
}