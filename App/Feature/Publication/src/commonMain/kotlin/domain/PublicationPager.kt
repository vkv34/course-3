package domain

import app.cash.paging.*
import model.ListResponse
import model.Publication
import model.PublicationDto
import ru.online.education.app.core.util.coruotines.suspendMap
import util.ApiResult

class PublicationPager(
    private val source: suspend (page: Int) -> ApiResult<ListResponse<PublicationDto>>,
    private val mapper: suspend (dto: PublicationDto) -> ApiResult<Publication>
) : PagingSource<Int, Publication>() {
    override fun getRefreshKey(state: PagingState<Int, Publication>): Int? = null

    override suspend fun load(params: PagingSourceLoadParams<Int>): PagingSourceLoadResult<Int, Publication> {
        val page = params.key ?: 0
        val response = source(page)
        return when (response) {
            is ApiResult.Empty -> PagingSourceLoadResultInvalid<Int, Publication>() as PagingSourceLoadResult<Int, Publication>
            is ApiResult.Error -> PagingSourceLoadResultError<Int, Publication>(
                Exception(
                    response.message,
                    response.throwable
                )
            ) as PagingSourceLoadResult<Int, Publication>

            is ApiResult.Success -> {
                val mapped = response.data.values.suspendMap { mapper(it) }
                if (mapped.any({ it is ApiResult.Error })) {
                    return PagingSourceLoadResultError<Int, Publication>(
                        Exception(
                            mapped.first { it is ApiResult.Error }.message,
                            (mapped.first { it is ApiResult.Error } as ApiResult.Error<Publication>).throwable
                        )
                    ) as PagingSourceLoadResult<Int, Publication>
                } else {
                    createPagingSourceLoadResultPage<Int, Publication>(
                        mapped
                            .filterIsInstance<ApiResult.Success<Publication>>()
                            .map { it.data },
                        prevKey = (page - 1).takeIf { it > 0 },
                        nextKey = if (response.data.values.isNotEmpty()) page.inc().takeIf { it > 0 } else null
                    ) as PagingSourceLoadResult<Int, Publication>
                }

            }
        }
    }


}