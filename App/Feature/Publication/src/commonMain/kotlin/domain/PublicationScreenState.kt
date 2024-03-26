package domain

import androidx.compose.runtime.Immutable
import app.cash.paging.cachedIn
import app.cash.paging.createPager
import app.cash.paging.createPagingConfig
import domain.mapper.toPublication
import kotlinx.coroutines.CoroutineScope
import repository.PublicationRepository
import repository.UserRepository
import util.map

@Immutable
class PublicationScreenState(
    private val publicationRepository: PublicationRepository,
    private val userRepository: UserRepository,
    private val courseId: Int,
    private val scope: CoroutineScope
) {

    private val pager = PublicationPager(
        source = { page -> publicationRepository.getByCourseId(courseId, page) },
        mapper = { dto ->
            val user = userRepository.getById(dto.authorId)
            user.map {
                dto.toPublication(it.fio)
            }
        }
    )

    val publications = createPager(
        config = createPagingConfig(publicationRepository.pageSize),
        initialKey = 0
    ) {
        pager
    }
        .flow
        .cachedIn(scope)

    fun reload() {
        pager.invalidate()
    }
}