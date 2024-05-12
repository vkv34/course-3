package navgation.destination.root.course.details

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.sample.shared.multipane.details.CourseDetailsComponent
import ru.online.education.app.core.util.compose.debbugable
import ru.online.education.app.core.util.model.ApiState
import ru.online.education.app.feature.course.domain.model.Course
import ru.online.education.app.feature.publication.presentation.EditPublicationDialog
import ru.online.education.app.feature.publication.presentation.PublicationListScreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CourseDetailsContent(
    context: CourseDetailsComponent
) {
    val publicationComponent = context.publicationComponent

    val currentCourse by context.currentCourse.subscribeAsState()
    val isToolBarVisible by context.isToolbarVisible.collectAsState(initial = null)
    val editable by context.canEdit.collectAsState()
    Box(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)

    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .debbugable()
        ) {
            TopAppBar(
                title = {
                    Crossfade(
                        currentCourse,
                        modifier = Modifier
                            .animateContentSize()
                            .debbugable()
                    ) { course ->
                        when (course) {
                            is ApiState.Default -> Unit
                            is ApiState.Error -> Text(course.error.message ?: "Error")
                            is ApiState.Loading -> CircularProgressIndicator(
                                modifier = Modifier.debbugable()
                            )

                            is ApiState.Success -> FlowRow {
                                Text(
                                    text = course.data.name,
                                    modifier = Modifier.debbugable()
                                )

                                Spacer(Modifier.width(16.dp))
                                Row {
                                    Text(
                                        text = "Код курса: ",
                                        modifier = Modifier.debbugable()
                                    )
                                    Text(
                                        text = course.data.id.toString(),
                                        modifier = Modifier.debbugable(),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Row {
                                    Text(
                                        text = "Категория курса: ",
                                        modifier = Modifier.debbugable(),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = course.data.courseCategory,
                                        modifier = Modifier.debbugable(),
                                        fontWeight = FontWeight.Bold
                                    )
                                }


                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = context::onCloseClicked) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            context.screenState.reload()
                            context.screenState.reloadUsersOnCourse()
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, null)
                    }
                },
                modifier = Modifier.debbugable()
            )


            PublicationListScreen(
                state = context.screenState,
                editable = editable,
                onEditClick = {
                    context.openEditDialog(it.publicationInCourseId)
                },
                onDeleteClick = {
                    context.deletePublication(it.publicationInCourseId)
                },
                courseName = (currentCourse as? ApiState.Success<Course>)?.data?.name ?: "",
                onCourseNameChange = {
                    context.updateCourse(it)
                    context.screenState.reload()
                }
            )


        }

        if (editable) {
            FloatingActionButton(
                onClick = context::openAddPublicationDialog,
                modifier = Modifier.align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PostAdd,
                    contentDescription = null
                )
            }
        }
    }

    val addingDialog by context.publicationDialogChild.subscribeAsState()

    addingDialog.child?.instance?.also { dialog ->
        EditPublicationDialog(
            publicationState = dialog.dialogState,
            onDismiss = context::closeAddPublicationDialog,
            attachmentListState = dialog.attachmentListState
        )
    }
}