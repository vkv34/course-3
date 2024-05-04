package ru.online.education.domain.repository

import ru.online.education.domain.repository.model.CourseCategoryDto
import ru.online.education.domain.repository.model.ListResponse
import ru.online.education.domain.repository.defaults.Repository
import util.ApiResult

interface CourseCategoryRepository: Repository<CourseCategoryDto, Int> {
    suspend fun findByName(name: String, page: Int): ApiResult<ListResponse<CourseCategoryDto>>
}