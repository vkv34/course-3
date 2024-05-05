package navgation.destination.root.course.list

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
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
                    containerColor = MaterialTheme.colorScheme.surfaceTint
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
            } else {
                var opened by remember { mutableStateOf(false) }
                ExtendedFloatingActionButton(
                    text = {
                        Text(text = "Присоединиться к курсу")
                    },
                    icon = {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    },
                    onClick = {
                        opened = !opened
                    },
                )

                if (opened) {
                    Popup(
                        onDismissRequest = {
                            opened = !opened
                        }
                    ) {
                        Card {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                var text by remember { mutableStateOf("") }
                                val loading by context.loading.subscribeAsState()
                                val error by context.addError.subscribeAsState()
                                OutlinedTextField(
                                    value = text,
                                    onValueChange = {
                                        text = it
                                    },
                                    label = {
                                        Text(
                                            text = "Код курса"
                                        )
                                    },
                                    isError = error.isNotBlank(),
                                    supportingText = {
                                        if (error.isNotBlank()) {
                                            Text(error)
                                        }
                                    }
                                )
                                TextButton(
                                    onClick = {
                                        context.tryToJoinCourse(text.toIntOrNull() ?: 0)
                                    },
                                    enabled = loading
                                ) {
                                    Text("Присоединится к курсу")
                                }
                            }
                        }
                    }
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