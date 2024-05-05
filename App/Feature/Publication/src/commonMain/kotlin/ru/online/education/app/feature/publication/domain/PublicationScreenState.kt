package ru.online.education.app.feature.publication.domain

import androidx.compose.runtime.Immutable
import app.cash.paging.cachedIn
import app.cash.paging.createPager
import app.cash.paging.createPagingConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.online.education.app.core.util.coruotines.DispatcherProvider
import ru.online.education.app.feature.publication.domain.mapper.toPublication
import ru.online.education.app.feature.publicationAttachment.domain.AnswerAttachmentListState
import ru.online.education.domain.repository.*
import ru.online.education.domain.repository.model.PublicationAnswerAttachmentDto
import ru.online.education.domain.repository.model.UserOnCourseDto
import ru.online.education.domain.repository.model.UserRole
import util.map
import kotlin.time.Duration.Companion.milliseconds


@Immutable
class PublicationScreenState(
    private val publicationRepository: PublicationRepository,
    private val userOnCourseRepository: UserOnCourseRepository,
    private val publicationAnswerRepository: PublicationAnswerRepository,
    private val accountRepository: AccountRepository,
    private val userRepository: UserRepository,
    private val answerAttachmentRepository: AnswerAttachmentRepository,
    private val courseId: Int,
    private val scope: CoroutineScope
) {

    data class ScreenState(
        val tab: Tab = Tab.Publications
    ) {
        enum class Tab(val tabName: String) {
            Publications("Публикации"),
            Users("Пользователи");

            companion object {
                val all = arrayOf(Publications, Users)
            }
        }
    }

    val screenState = MutableStateFlow(ScreenState())

    fun selectTab(tab: ScreenState.Tab) {
        screenState.update { it.copy(tab = tab) }
    }

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

    val userPager = MutableStateFlow(
        UsersOnCoursePager(
            source = { page ->
                userOnCourseRepository
                    .getUsersOnCourseByCourseId(
                        courseId = courseId,
                        page = page
                    )
            }
        )
    )

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val usersOnCourse = userPager
        .debounce(300.milliseconds)
        .flatMapLatest {
            createPager(
                config = createPagingConfig(userOnCourseRepository.pageSize),
                initialKey = 0
            ) {
                it
            }
                .flow
                .cachedIn(scope)
        }

    fun reloadUsersOnCourse() {
        userPager.value = UsersOnCoursePager { page ->
            userOnCourseRepository
                .getUsersOnCourseByCourseId(
                    courseId = courseId,
                    page = page
                )
        }
    }

    fun changeUserOnCourseRole(
        userOnCourseDto: UserOnCourseDto,
        newRole: UserRole
    ) {
        scope.launch {
            userOnCourseRepository.update(userOnCourseDto.copy(role = newRole))
            reloadUsersOnCourse()

        }
    }

    fun deleteUserFromCourse(
        userOnCourseId: Int
    ) {
        scope.launch {
            userOnCourseRepository.deleteById(userOnCourseId)
            reloadUsersOnCourse()

        }
    }

    fun getPublicationAnswerState(
        publicationOnCourseId: Int
    ) = AnswerListState(
        publicationAnswerRepository = publicationAnswerRepository,
        accountRepository = accountRepository,
        publicationOnCourseId = publicationOnCourseId,
        coroutineScope = scope
    )


    fun createAnswerAttachmentListState() =
        AnswerAttachmentListState(
            repository = answerAttachmentRepository,
            coroutineScope = scope
        )

    val answers = MutableStateFlow(mapOf<Int, List<PublicationAnswerAttachmentDto>>())

    fun clearAnswers(){
        answers.update { mapOf() }
    }

    fun loadAnswerAttachments(answerId: Int) {
        scope.launch(DispatcherProvider.IO) {
            val result = answerAttachmentRepository.getAttachments(answerId)

            if (result.successOrNull() != null) {
                answers.update { it.plus(answerId to result.successOrNull()!!.values) }
            }
        }
    }

}