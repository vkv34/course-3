package com.arkivanov.sample.shared.multipane.details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import destination.course.publication.PublicationComponent
import destination.course.publication.create.PublicationDialogComponent
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import ru.online.education.app.core.util.api.toApiState
import ru.online.education.app.core.util.coruotines.DispatcherProvider
import ru.online.education.app.core.util.model.ApiState
import ru.online.education.app.feature.account.domain.repository.UserAuthStore
import ru.online.education.app.feature.course.domain.model.Course
import ru.online.education.app.feature.course.domain.model.mapper.toCourse
import ru.online.education.app.feature.publication.domain.PublicationScreenState
import ru.online.education.domain.repository.*
import ru.online.education.domain.repository.model.UserRole


class CourseDetailsComponent(
    componentContext: ComponentContext,
    val courseId: Long,
    val isToolbarVisible: Flow<Boolean>,
    private val onFinished: () -> Unit,
    val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
) : ComponentContext by componentContext, KoinComponent {

    private val courseRepository: CourseRepository by inject()

    val currentCourse = MutableValue<ApiState<Course>>(ApiState.Default<Course>())

    private val _canEdit = MutableStateFlow(false)
    val canEdit = _canEdit.asStateFlow()

    private val accountRepository by inject<AccountRepository>()
    private val authStore by inject<UserAuthStore>()
    val userOnCourseRepository by inject<UserOnCourseRepository>()

    init {

        coroutineScope.launch {
            val course = courseRepository.getById(courseId.toInt())
            withContext(DispatcherProvider.Main) {
                currentCourse.update { course.toApiState { it.toCourse() } }
            }
            val user = withContext(DispatcherProvider.IO) {
                accountRepository.currentUser()
            }
            val usersOnCourse =
                userOnCourseRepository.getUsersOnCourseByCourseId(courseId = courseId.toInt(), page = 0)
                    .successOrNull()
            _canEdit.value = user.successOrNull()?.id == course.successOrNull()?.creatorId
                    || (usersOnCourse != null
                    && usersOnCourse.values.any { it.role != UserRole.Student && it.userDto.id == user.successOrNull()?.id })

        }

        coroutineScope.launch(DispatcherProvider.IO) {
            authStore.readAsFlow().collect {
                val user = withContext(DispatcherProvider.IO) {
                    accountRepository.currentUser()
                }
                val course = withContext(DispatcherProvider.IO) { courseRepository.getById(courseId.toInt()) }
                val usersOnCourse =
                    userOnCourseRepository.getUsersOnCourseByCourseId(courseId = courseId.toInt(), page = 0)
                        .successOrNull()
                _canEdit.value = user.successOrNull()?.id == course.successOrNull()?.creatorId
                        || (usersOnCourse != null
                        && usersOnCourse.values.any { it.role != UserRole.Student && it.userDto.id == user.successOrNull()?.id })
            }
        }
    }

    private val publicationRepository: PublicationRepository by inject()
    private val publicationOnCourseRepository: PublicationOnCourseRepository by inject()
    private val userRepository: UserRepository by inject()

    val screenState = PublicationScreenState(
        publicationRepository = publicationRepository,
        userOnCourseRepository = get(),
        publicationAnswerRepository = get(),
        accountRepository = get(),
        answerAttachmentRepository = get(),
        userRepository = userRepository,
        courseId = courseId.toInt(),
        scope = coroutineScope
    )


    private val addPublicationDialogNavigation = SlotNavigation<Unit>()

    val publicationDialogChild = childSlot(
        source = addPublicationDialogNavigation,
        serializer = null,
    ) { configuration, componentContext ->
        PublicationDialogComponent(
            componentContext = componentContext,
            courseId = courseId.toInt(),
            coroutineScope = coroutineScope,
            onDismiss = ::closeAddPublicationDialog
        )
    }

    fun openEditDialog(publicationId: Int) {
        addPublicationDialogNavigation.activate(Unit)
        publicationDialogChild
            .value
            .child
            ?.instance
            ?.openPublication(publicationId)

    }

    fun openAddPublicationDialog() {
        addPublicationDialogNavigation.activate(Unit)
    }

    fun closeAddPublicationDialog() {
        addPublicationDialogNavigation.dismiss()
        screenState.reload()
    }


    val publicationComponent = PublicationComponent(componentContext)
    fun onCloseClicked() {
        onFinished()
    }

    fun deletePublication(publicationInCourseId: Int) {
        coroutineScope.launch(DispatcherProvider.IO) {
            publicationOnCourseRepository.deleteById(publicationInCourseId)
            screenState.reload()
        }
    }


}
