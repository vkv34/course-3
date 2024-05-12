package presentation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import presentation.viewModel.AddCourseCategoryDialogState
import ru.online.education.app.core.util.compose.AdaptiveDialog
import ru.online.education.app.core.util.compose.debbugable

@Composable
fun CourseCategoryAddDialog(
    courseCategoryDialogState: AddCourseCategoryDialogState,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val screenState by courseCategoryDialogState.screenState.collectAsState()
    AdaptiveDialog(
        onDismiss = onDismissRequest,
        modifier = modifier
            .wrapContentHeight()
//            .width(300.dp)
            .debbugable()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .verticalScroll(rememberScrollState())
                .debbugable(),
            horizontalAlignment = Alignment.Start,

            ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = onDismissRequest
                ) {
                    Icon(Icons.Filled.Clear, contentDescription = "Close")
                }
            }
            Text(
                "Добавить категорию",
                style = MaterialTheme.typography.titleLarge
            )
            HorizontalDivider(modifier = Modifier.fillMaxWidth())

            OutlinedTextField(
                value = screenState.name,
                onValueChange = courseCategoryDialogState::updateName,
                label = { Text("Название категории") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = screenState.description,
                onValueChange = courseCategoryDialogState::updateDescription,
                label = { Text("Описание") },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Crossfade(screenState.error) { error ->
                if (error.isNotBlank()) {
                    Text(
                        text = error,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            Button(
                onClick = courseCategoryDialogState::addCategory,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Добавить")
            }

        }
    }
}