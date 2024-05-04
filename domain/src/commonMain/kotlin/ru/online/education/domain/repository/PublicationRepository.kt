package ru.online.education.domain.repository

import ru.online.education.domain.repository.model.PublicationDto
import ru.online.education.domain.repository.model.ListResponse
import ru.online.education.domain.repository.defaults.Repository
import util.ApiResult

interface PublicationRepository: Repository<PublicationDto, Int> {

    override val pageSize: Int
        get() = 3

    suspend fun getByCourseId(courseId: Int, page: Int): ApiResult<ListResponse<PublicationDto>>

    suspend fun getByPublicationOnCourseId(publicationOnCourseId: Int): ApiResult<PublicationDto>
}