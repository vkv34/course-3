package ru.online.education.app.feature.course.presentation.viewModel

import androidx.compose.runtime.Stable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import model.CourseCategory
import presentation.viewModel.CourseCategorySearchBarState
import ru.online.education.domain.repository.CourseCategoryRepository
import ru.online.education.domain.repository.CourseRepository
import ru.online.education.app.core.util.coruotines.DispatcherProvider
import ru.online.education.app.feature.course.domain.model.Course
import ru.online.education.app.feature.course.domain.model.mapper.toCourse
import ru.online.education.app.feature.course.domain.model.mapper.toCourseDto
import util.ApiResult

@Stable
class CreateCourseDialogState(
    private val courseRepository: CourseRepository,
    private val courseCategoryRepository: CourseCategoryRepository,
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

    private val _courseCategory = MutableStateFlow(CourseCategory())
    val courseCategory = _courseCategory.asStateFlow()

    val searchBarState = CourseCategorySearchBarState(
        courseCategoryRepository = courseCategoryRepository,
        coroutineScope = coroutineScope
    )

    init {
        coroutineScope.launch {
            searchBarState.selectedCourseCategory
                .filterNotNull()
                .collect { selectedCategory ->
                    _courseCategory.update { selectedCategory }
                }
        }
    }

    fun setCourseCategory(category: CourseCategory) {
        _courseCategory.update { category }
    }

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
            val result = createCourse(_courseState.value, _courseCategory.value.id)

            if (result is ApiResult.Success) {
                val createdCourse = result.data
                onSuccess(createdCourse.toCourse())
            } else {
                _screenState.update { it.copy(error = result.message) }
            }
        }
    }

    private suspend fun createCourse(course: Course, courseCategoryId: Int) =
        courseRepository
            .add(
                course.toCourseDto()
                    .copy(courseCategoryId = courseCategoryId)
            )


}