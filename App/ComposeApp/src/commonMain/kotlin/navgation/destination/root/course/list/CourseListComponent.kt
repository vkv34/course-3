package navgation.destination.root.course.list

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import destination.course.list.CourseListComponent
import presentation.CourseCategoryAddDialog
import ru.online.education.app.core.util.compose.debbugable
import ru.online.education.app.feature.course.presentation.ui.CoursesList
import ru.online.education.app.feature.course.presentation.ui.CreateCourseDialog

@Composable
fun CourseListComponent(
    context: CourseListComponent
) {
    Box(
        modifier = Modifier.fillMaxSize()
            .debbugable()
    ) {
        CoursesList(
            modifier = Modifier
                .fillMaxSize()
                .debbugable(),
            onCourseClick = { course -> context.onCourseClicked(course.id.toLong()) },
            allCoursesScreenViewModel = context.allCoursesViewModel
        )

        val screenState by context.allCoursesViewModel.screenState.collectAsState()

        Crossfade(
            screenState.canEdit,
            modifier = Modifier
                .padding(end = 16.dp, bottom = 16.dp)
                .align(Alignment.BottomEnd)
        ) { canEdit ->
            if (canEdit) {
                FloatingActionButton(
                    onClick = context::openCreateDialog,
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
            }
        }
    }

    val state by context.createCourseDialog.subscribeAsState()

    state.child?.instance?.also { createCourseDialogComponent ->
        val addCategoryDialog by createCourseDialogComponent.addCategoryChild.subscribeAsState()
        addCategoryDialog.child?.instance?.also { addCourseCategoryDialogComponent ->
            CourseCategoryAddDialog(
                courseCategoryDialogState = addCourseCategoryDialogComponent.addCourseCategoryDialogState,
                onDismissRequest = createCourseDialogComponent::closeAddCategoryDialog,
            )
        }
        CreateCourseDialog(
            courseDialogState = createCourseDialogComponent.dialogState,
            onDismiss = createCourseDialogComponent.onDismiss,
            onAddCategoryClick = createCourseDialogComponent::openAddCategoryDialog
        )
    }
}