package destination.course.list.create

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import destination.courseCategory.dialog.addDialog.AddCourseCategoryDialogComponent
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
        courseCategoryRepository = get(),
        coroutineScope = coroutineScope,
        onSuccess = onSuccess
    )

    private val navigation = SlotNavigation<Unit>()

    val addCategoryChild = childSlot(
        source = navigation,
        serializer = null,
    ) { config, context ->
        AddCourseCategoryDialogComponent(
            context = context,
            coroutineScope = coroutineScope,
            onSuccess = {
                dialogState.setCourseCategory(it)
                closeAddCategoryDialog()
            },
            onDismissRequest = onDismiss
        )
    }


    fun openAddCategoryDialog() {
        navigation.activate(Unit)
    }

    fun closeAddCategoryDialog() {
        navigation.dismiss()
    }


}