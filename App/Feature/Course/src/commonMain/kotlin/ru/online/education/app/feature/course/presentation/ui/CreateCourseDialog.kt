package ru.online.education.app.feature.course.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.online.education.app.core.util.compose.AdaptiveDialog
import ru.online.education.app.core.util.compose.debbugable
import ru.online.education.app.feature.course.presentation.viewModel.CreateCourseDialogState

@Composable
fun CreateCourseDialog(
    courseDialogState: CreateCourseDialogState,
    onDismiss: () -> Unit
) {
    val screenState by courseDialogState.screenState.collectAsState()
    val courseState by courseDialogState.courseState.collectAsState()

    AdaptiveDialog(
        onDismiss = onDismiss,
        modifier = Modifier
            .fillMaxSize()
            .debbugable()
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .debbugable(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .debbugable(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Наименование",
                    modifier = Modifier.debbugable()
                )
                OutlinedTextField(
                    value = courseState.name,
                    onValueChange = courseDialogState::onNameChange,
                    modifier = Modifier.fillMaxWidth()
                        .debbugable()
                )
            }
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 4.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .debbugable(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Короткое описание",
                    modifier = Modifier.debbugable()
                )
                OutlinedTextField(
                    value = courseState.shortDescription,
                    onValueChange = courseDialogState::onShortDescriptionChange,
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                        .debbugable()
                )
            }
        }
    }
}