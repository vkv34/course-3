package destination.courseCategory.dialog.addDialog

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import model.CourseCategory
import ru.online.education.domain.repository.model.CourseCategoryDto
import ru.online.education.domain.repository.model.CourseDto
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import presentation.viewModel.AddCourseCategoryDialogState
import util.ApiResult

class AddCourseCategoryDialogComponent(
    context: ComponentContext,
    private val coroutineScope: CoroutineScope,
    private val onSuccess: (CourseCategory) -> Unit,
    private val onDismissRequest: () -> Unit
): ComponentContext by context, KoinComponent {

    val addCourseCategoryDialogState = AddCourseCategoryDialogState(
        courseCategoryRepository = get(),
        scope = coroutineScope,
        onSuccess = onSuccess,
    )
}