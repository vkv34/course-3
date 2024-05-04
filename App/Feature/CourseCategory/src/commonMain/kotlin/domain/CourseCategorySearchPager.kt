package domain

import app.cash.paging.*
import domain.mapper.toCourseCategory
import model.CourseCategory
import ru.online.education.domain.repository.model.CourseCategoryDto
import ru.online.education.domain.repository.model.ListResponse
import util.ApiResult

class CourseCategorySearchPager(
    private val query: String,
    private val source: suspend (query: String, page: Int) -> ApiResult<ListResponse<CourseCategoryDto>>
) : PagingSource<Int, CourseCategory>() {
    override fun getRefreshKey(state: PagingState<Int, CourseCategory>): Int? = null

    override suspend fun load(params: PagingSourceLoadParams<Int>): PagingSourceLoadResult<Int, CourseCategory> {
        val page = params.key ?: 0
        val response = source(query, page)
        return when (response) {
            is ApiResult.Empty -> PagingSourceLoadResultInvalid<Int, CourseCategory>() as PagingSourceLoadResult<Int, CourseCategory>
            is ApiResult.Error -> PagingSourceLoadResultError<Int, CourseCategory>(
                Exception(
                    response.message,
                    response.throwable
                )
            ) as PagingSourceLoadResult<Int, CourseCategory>

            is ApiResult.Success -> {
                val mapped = response.data.values.map(CourseCategoryDto::toCourseCategory)
                createPagingSourceLoadResultPage<Int, CourseCategory>(
                    mapped,
                    prevKey = (page - 1).takeIf { it > 0 },
                    nextKey = if (response.data.values.isNotEmpty()) page.inc().takeIf { it > 0 } else null
                ) as PagingSourceLoadResult<Int, CourseCategory>

            }
        }
    }

}