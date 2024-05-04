package presentation.viewModel

import domain.mapper.toCourseCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.CourseCategory
import ru.online.education.domain.repository.model.CourseCategoryDto
import ru.online.education.domain.repository.CourseCategoryRepository
import util.ApiResult

class AddCourseCategoryDialogState(
    private val courseCategoryRepository: CourseCategoryRepository,
    private val scope: CoroutineScope,
    private val onSuccess: (CourseCategory) -> Unit
) {
    data class ScreenState(
        val name: String = "",
        val description: String = "",
        val error: String = ""
    )

    private val _screenState = MutableStateFlow(ScreenState())
    val screenState = _screenState.asStateFlow()

    fun updateName(name: String) {
        _screenState.update { it.copy(name = name) }
    }

    fun updateDescription(description: String) {
        _screenState.update { it.copy(description = description) }
    }

    fun addCategory() {
        scope.launch {
            val result = courseCategoryRepository
                .add(CourseCategoryDto(name = _screenState.value.name, description = _screenState.value.description))
            if (result is ApiResult.Success) {
                _screenState.update { it.copy(error = "") }
                onSuccess(result.data.toCourseCategory())
            } else {
                _screenState.update { it.copy(error = (result as ApiResult.Error).message) }
            }
        }
    }
}