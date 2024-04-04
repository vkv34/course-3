package ru.online.education.domain.services.courseCategory

import model.CourseCategoryDto
import model.ListResponse
import repository.CourseCategoryRepository
import ru.online.education.domain.model.BaseService
import util.ApiResult

class CourseCategoryService(
    val courseCategoryRepository: CourseCategoryRepository,
) : BaseService<CourseCategoryDto, Int?> {
    override suspend fun create(data: CourseCategoryDto) = courseCategoryRepository.add(data)

    override suspend fun update(data: CourseCategoryDto): ApiResult<Int?> {
        TODO("Not yet implemented")
    }

    override suspend fun getAll(page: Int): ApiResult<ListResponse<CourseCategoryDto>> = courseCategoryRepository.getAll(page)

    override suspend fun getById(id: Int): ApiResult<CourseCategoryDto> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteById(id: Int): ApiResult<Int?> {
        TODO("Not yet implemented")
    }
}
