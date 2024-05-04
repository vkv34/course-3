package ru.online.education.app.feature.publication.domain

import app.cash.paging.*
import ru.online.education.domain.repository.model.ListResponse
import ru.online.education.domain.repository.model.UserOnCourseDto
import util.ApiResult

class UsersOnCoursePager(
    private val source: suspend (page: Int) -> ApiResult<ListResponse<UserOnCourseDto>>,
//    private val mapper: suspend (dto: PublicationDto) -> ApiResult<UserOnCourseDto>
) : PagingSource<Int, UserOnCourseDto>() {
    override fun getRefreshKey(state: PagingState<Int, UserOnCourseDto>): Int? = null

    override suspend fun load(params: PagingSourceLoadParams<Int>): PagingSourceLoadResult<Int, UserOnCourseDto> {
        val page = params.key ?: 0
        val response = source(page)
        return when (response) {
            is ApiResult.Empty -> PagingSourceLoadResultInvalid<Int, UserOnCourseDto>() as PagingSourceLoadResult<Int, UserOnCourseDto>
            is ApiResult.Error -> PagingSourceLoadResultError<Int, UserOnCourseDto>(
                Exception(
                    response.message,
                    response.throwable
                )
            ) as PagingSourceLoadResult<Int, UserOnCourseDto>

            is ApiResult.Success -> createPagingSourceLoadResultPage<Int, UserOnCourseDto>(
                response.data.values,
                prevKey = (page - 1).takeIf { it > 0 },
                nextKey = if (response.data.values.isNotEmpty()) page.inc().takeIf { it > 0 } else null
            ) as PagingSourceLoadResult<Int, UserOnCourseDto>
        }
    }


}