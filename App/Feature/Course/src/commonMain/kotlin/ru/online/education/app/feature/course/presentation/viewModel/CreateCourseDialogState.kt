package ru.online.education.app.feature.course.presentation.viewModel

import androidx.compose.runtime.Stable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import repository.CourseRepository
import ru.online.education.app.core.util.coruotines.DispatcherProvider
import ru.online.education.app.feature.course.domain.model.Course
import ru.online.education.app.feature.course.domain.model.mapper.toCourse
import ru.online.education.app.feature.course.domain.model.mapper.toCourseDto
import util.ApiResult

@Stable
class CreateCourseDialogState(
    private val courseRepository: CourseRepository,
    private val coroutineScope: CoroutineScope,
    private val onSuccess: (createdCourse: Course) -> Unit,
//    private val onDismiss: () -> Unit
) {

    data class ScreenState(
        val error: String = ""
    )

    private val _courseState = MutableStateFlow(Course())
    val courseState = _courseState.asStateFlow()

    private val _screenState = MutableStateFlow(ScreenState())
    val screenState = _screenState.asStateFlow()

    fun onNameChange(name: String) {
        _courseState.update { it.copy(name = name) }
    }

    fun onShortDescriptionChange(shortDescription: String) {
        _courseState.update { it.copy(shortDescription = shortDescription) }
    }

    fun onLongDescriptionChange(longDescription: String) {
        _courseState.update { it.copy(longDescription = longDescription) }
    }

    fun onDateCreateChange(dateCreate: LocalDateTime) {
        _courseState.update { it.copy(dateCreate = dateCreate) }
    }

//    fun onAvatarChange(avatar: Image) {
//        _courseState.update { it.copy(avatar = avatar) }
//    }

//    fun onBackgroundChange(background: Image) {
//        _courseState.update { it.copy(background = background) }
//    }

    fun onAddClick() {
        coroutineScope.launch(DispatcherProvider.IO) {
            val result = createCourse(_courseState.value)

            if (result is ApiResult.Success) {
                val createdCourse = result.data
                onSuccess(createdCourse.toCourse())
            } else {
                _screenState.update { it.copy(error = result.message) }
            }
        }
    }

    private suspend fun createCourse(course: Course) =
        courseRepository.add(course.toCourseDto())


}