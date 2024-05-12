package ru.online.education.app.feature.publication.domain

import androidx.compose.runtime.Stable
import app.cash.paging.cachedIn
import app.cash.paging.createPager
import app.cash.paging.createPagingConfig
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import ru.online.education.app.core.util.coruotines.DispatcherProvider
import ru.online.education.domain.repository.AccountRepository
import ru.online.education.domain.repository.PublicationAnswerRepository
import ru.online.education.domain.repository.model.PublicationAnswerDto
import ru.online.education.domain.repository.model.UserRole
import util.ApiResult
import kotlin.time.Duration.Companion.milliseconds

@Stable
class AnswerListState(
    private val publicationAnswerRepository: PublicationAnswerRepository,
    private val accountRepository: AccountRepository,
    private val publicationOnCourseId: Int,
    private val coroutineScope: CoroutineScope
) {

    val answersPager = MutableStateFlow(
        createAnswerPager()
    )

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val answers = answersPager
        .debounce(300.milliseconds)
        .flatMapLatest {
            createPager(
                config = createPagingConfig(publicationAnswerRepository.pageSize),
                initialKey = 0
            ) {
                it
            }
                .flow
//                .cachedIn(coroutineScope)
        }

    fun createAnswerPager() = PublicationAnswerPager(
        source = { page ->
            val currentUser = accountRepository.currentUser().successOrNull()
            if (currentUser != null) {
                if (currentUser.role == UserRole.Student) {
                    publicationAnswerRepository.getAllByPublicationOnCourseId(
                        publicationOnCourseId = publicationOnCourseId,
                        page = page,
                        userId = currentUser.id
                    )
                } else {
                    publicationAnswerRepository.getAllByPublicationOnCourseId(
                        publicationOnCourseId = publicationOnCourseId,
                        page = page
                    )
                }
            } else {
                ApiResult.Empty(message = "Вы не авторизованы")
            }

        }
    )

    fun refreshAnswers() {
        answersPager.update { createAnswerPager() }
    }

    fun addAnswer(
        answer: String,
        onAdd: (Int) -> Unit
    ) = coroutineScope.launch {
//        val currentUser =
//            withContext(DispatcherProvider.IO) { accountRepository.currentUser().successOrNull() } ?: return@launch


        withContext(DispatcherProvider.IO) {
            val result = publicationAnswerRepository.add(
                PublicationAnswerDto(
                    answer = answer,
                    publicationOnCourseId = publicationOnCourseId
                )
            )



            if (result is ApiResult.Success) {
                onAdd(result.data.id)
            }
        }

        refreshAnswers()
    }

    fun sendMarkAndComment(
        mark: Byte? = null,
        comment: String? = null,
        answerId: Int
    ) {
        coroutineScope.launch(DispatcherProvider.IO) {
            publicationAnswerRepository.sendMarkAndComment(mark = mark, comment = comment, answerId = answerId)

            refreshAnswers()
        }
    }

}