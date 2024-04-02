package ru.online.education.app.core.util.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ConfirmDialogCard(
    title: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    subTitle: String? = null,
) {
    ElevatedCard(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 5.dp
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
                .debbugable()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = contentColorFor(MaterialTheme.colorScheme.surfaceContainerHighest)
            )
            if (subTitle != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = subTitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = contentColorFor(MaterialTheme.colorScheme.surfaceContainerHighest)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TextButton(
                    onClick = onConfirm,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    contentPadding = PaddingValues(2.dp)
                ) {
                    Text("Да")
                }
                TextButton(
                    onClick = onDismiss,
                    contentPadding = PaddingValues(2.dp)
                ) {
                    Text("Нет")
                }
            }

        }
    }
}

