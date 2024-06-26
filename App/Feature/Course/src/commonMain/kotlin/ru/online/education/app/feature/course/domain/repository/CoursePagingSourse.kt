package ru.online.education.app.feature.course.domain.repository

import app.cash.paging.*
import ru.online.education.domain.repository.model.CourseDto
import ru.online.education.domain.repository.model.ListResponse
import ru.online.education.app.core.util.coruotines.suspendMap
import ru.online.education.app.feature.course.domain.model.Course
import ru.online.education.app.feature.course.domain.model.mapper.toCourse
import util.ApiResult

class CoursePagingSource(
    private val source: suspend (page: Int) -> ApiResult<ListResponse<CourseDto>>,
    private val authorSource: suspend (authorId: Int) -> String
) : PagingSource<Int, Course>() {
    override fun getRefreshKey(state: PagingState<Int, Course>): Int? = null
    /*state.anchorPosition?.let { anchorPosiiton ->
        val anchorPage = state.closestPageToPosition(anchorPosiiton)
        anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
    }*/

    override suspend fun load(params: PagingSourceLoadParams<Int>): PagingSourceLoadResult<Int, Course> {
        val page = params.key ?: 0
        val response = source(page)
        return when (response) {
            is ApiResult.Empty -> PagingSourceLoadResultInvalid<Int, Course>() as PagingSourceLoadResult<Int, Course>
            is ApiResult.Error -> PagingSourceLoadResultError<Int, Course>(
                Exception(
                    response.message,
                    response.throwable
                )
            ) as PagingSourceLoadResult<Int, Course>

            is ApiResult.Success -> createPagingSourceLoadResultPage<Int, Course>(
                response.data.values
                    .suspendMap {
                        it.toCourse()
                            .copy(courseCreator = authorSource(it.creatorId ?: 0))
                    },
                prevKey = (page - 1).takeIf { it > 0 },
                nextKey = if (response.data.values.isNotEmpty()) page.inc().takeIf { it > 0 } else null
            ) as PagingSourceLoadResult<Int, Course>
        }
    }


}