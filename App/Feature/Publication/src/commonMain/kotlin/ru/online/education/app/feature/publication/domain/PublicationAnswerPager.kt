package ru.online.education.app.feature.publication.domain

import app.cash.paging.*
import ru.online.education.domain.repository.model.ListResponse
import ru.online.education.domain.repository.model.PublicationAnswerDto
import util.ApiResult


class PublicationAnswerPager(
    private val source: suspend (page: Int) -> ApiResult<ListResponse<PublicationAnswerDto>>,
//    private val mapper: suspend (dto: PublicationDto) -> ApiResult<PublicationAnswerDto>
) : PagingSource<Int, PublicationAnswerDto>() {
    override fun getRefreshKey(state: PagingState<Int, PublicationAnswerDto>): Int? = null

    override suspend fun load(params: PagingSourceLoadParams<Int>): PagingSourceLoadResult<Int, PublicationAnswerDto> {
        val page = params.key ?: 0
        val response = source(page)
        return when (response) {
            is ApiResult.Empty -> PagingSourceLoadResultInvalid<Int, PublicationAnswerDto>() as PagingSourceLoadResult<Int, PublicationAnswerDto>
            is ApiResult.Error -> PagingSourceLoadResultError<Int, PublicationAnswerDto>(
                Exception(
                    response.message,
                    response.throwable
                )
            ) as PagingSourceLoadResult<Int, PublicationAnswerDto>

            is ApiResult.Success -> createPagingSourceLoadResultPage<Int, PublicationAnswerDto>(
                response.data.values,
                prevKey = (page - 1).takeIf { it > 0 },
                nextKey = if (response.data.values.isNotEmpty()) page.inc().takeIf { it > 0 } else null
            ) as PagingSourceLoadResult<Int, PublicationAnswerDto>
        }
    }


}