package com.arkivanov.sample.shared.multipane.details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import destination.course.list.create.CreateCourseDialogComponent
import destination.course.publication.PublicationComponent
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
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

    init {
        coroutineScope.launch {
            val course = courseRepository.getById(courseId.toInt())
            withContext(DispatcherProvider.Main) {
                currentCourse.update { course.toApiState { it.toCourse() } }
            }
        }
    }

    val publicationComponent = PublicationComponent(componentContext)
    fun onCloseClicked() {
        onFinished()
    }

}
