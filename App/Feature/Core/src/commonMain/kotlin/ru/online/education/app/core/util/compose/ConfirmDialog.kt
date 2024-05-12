package ru.online.education.app.core.util.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup

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
            defaultElevation = 8.dp
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
                .debbugable()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
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

@Composable
fun ConfirmPopup(
    title: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    subTitle: String? = null,
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }

    Popup(
        onDismissRequest = onDismiss,
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            ConfirmDialogCard(
                title = title,
                subTitle = subTitle,
                onConfirm = onConfirm,
                onDismiss = onDismiss
            )
        }
    }
}

@Composable
fun ConfirmIconButton(
    icon: @Composable () -> Unit,
    title: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit = {},
    modifier: Modifier = Modifier,
    subTitle: String? = null,
) {
    var opened by remember { mutableStateOf(false) }
    IconButton(
        onClick = {
            opened = ! opened
        },
        modifier = modifier
    ){
        icon()

        if (opened){
            ConfirmPopup(
                title = title,
                subTitle = subTitle,
                onConfirm = {
                    onConfirm()
                    opened = false
                },
                onDismiss = {
                    onDismiss()
                    opened = false
                }
            )
        }
    }


}

