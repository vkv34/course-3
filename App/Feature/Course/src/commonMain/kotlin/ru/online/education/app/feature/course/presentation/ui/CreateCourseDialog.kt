package ru.online.education.app.feature.course.presentation.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import presentation.CourseCategorySearchBar
import ru.online.education.app.core.util.compose.AdaptiveDialog
import ru.online.education.app.core.util.compose.debbugable
import ru.online.education.app.feature.course.presentation.viewModel.CreateCourseDialogState

@Composable
fun CreateCourseDialog(
    courseDialogState: CreateCourseDialogState,
    onDismiss: () -> Unit,
    onAddCategoryClick: () -> Unit
) {
    val screenState by courseDialogState.screenState.collectAsState()
    val courseState by courseDialogState.courseState.collectAsState()

    AdaptiveDialog(
        onDismiss = onDismiss,
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .debbugable()
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .debbugable(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = courseState.name,
                onValueChange = courseDialogState::onNameChange,
                label = {
                    Text(
                        "Наименование",
                        modifier = Modifier.debbugable()
                    )
                },
                modifier = Modifier.fillMaxWidth()
                    .debbugable()
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
            OutlinedTextField(
                value = courseState.shortDescription,
                onValueChange = courseDialogState::onShortDescriptionChange,
                label = {
                    Text(
                        "Короткое описание",
                        modifier = Modifier.debbugable()
                    )
                },
                minLines = 2,
                modifier = Modifier.fillMaxWidth()
                    .debbugable()
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
            CourseCategorySearchBar(
                state = courseDialogState.searchBarState,
                modifier = Modifier.fillMaxWidth()
                    .debbugable(),
                canAdd = true,
                onAddClick = onAddCategoryClick
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
            Crossfade(screenState.error){ error ->
                if (error.isNotBlank()){
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .debbugable(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = courseDialogState::onAddClick,
                    modifier = Modifier.debbugable()
                ) {
                    Text("Добавить курс")
                }
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Отменить")
                }
            }
        }
    }
}