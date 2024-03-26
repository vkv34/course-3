package destination.course.details.create

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import ru.online.education.app.feature.course.domain.model.Course
import ru.online.education.app.feature.course.presentation.viewModel.CreateCourseDialogState

class CreateCourseDialogComponent(
    private val context: ComponentContext,
    private val coroutineScope: CoroutineScope,
    val onDismiss: () -> Unit,
    private val onSuccess: (Course) -> Unit,
) : KoinComponent, ComponentContext by context {
    val dialogState = CreateCourseDialogState(
        courseRepository = get(),
        coroutineScope = coroutineScope,
        onSuccess = onSuccess
    )


}