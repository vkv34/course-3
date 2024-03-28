package domain

import androidx.compose.runtime.Immutable
import app.cash.paging.cachedIn
import app.cash.paging.createPager
import app.cash.paging.createPagingConfig
import domain.mapper.toPublication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import repository.PublicationRepository
import repository.UserRepository
import util.map
import kotlin.time.Duration.Companion.milliseconds

@Immutable
class PublicationScreenState(
    private val publicationRepository: PublicationRepository,
    private val userRepository: UserRepository,
    private val courseId: Int,
    private val scope: CoroutineScope
) {

    private val pager = MutableStateFlow(PublicationPager(
        source = { page -> publicationRepository.getByCourseId(courseId, page) },
        mapper = { dto ->
            val user = userRepository.getById(dto.authorId)
            user.map {
                dto.toPublication(it.fio)
            }
        }
    ))

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val publications = pager
        .debounce(300.milliseconds)
        .flatMapLatest {
            createPager(
                config = createPagingConfig(publicationRepository.pageSize),
                initialKey = 0
            ) {
                it
            }
                .flow
                .cachedIn(scope)
        }


    fun reload() {
        pager.value = PublicationPager(
            source = { page -> publicationRepository.getByCourseId(courseId, page) },
            mapper = { dto ->
                val user = userRepository.getById(dto.authorId)
                user.map {
                    dto.toPublication(it.fio)
                }
            }
        )
    }
}