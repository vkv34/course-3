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
import org.koin.core.component.inject
import repository.AccountRepository
import repository.CourseRepository
import ru.online.education.app.core.util.api.toApiState
import ru.online.education.app.core.util.coruotines.DispatcherProvider
import ru.online.education.app.core.util.model.ApiState
import ru.online.education.app.feature.course.domain.model.Course
import ru.online.education.app.feature.course.domain.model.mapper.toCourse


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

    init {
        coroutineScope.launch {
            val course = courseRepository.getById(courseId.toInt())
            withContext(DispatcherProvider.Main) {
                currentCourse.update { course.toApiState { it.toCourse() } }
            }
            val user = accountRepository.currentUser()
            _canEdit.value = user.successOrNull()?.id == course.successOrNull()?.creatorId
        }
    }


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

    fun openAddPublicationDialog() {
        addPublicationDialogNavigation.activate(Unit)
    }

    fun closeAddPublicationDialog() {
        addPublicationDialogNavigation.dismiss()
    }


    val publicationComponent = PublicationComponent(componentContext)
    fun onCloseClicked() {
        onFinished()
    }

}
