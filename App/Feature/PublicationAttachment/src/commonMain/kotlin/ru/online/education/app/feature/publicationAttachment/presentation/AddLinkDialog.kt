package ru.online.education.app.feature.publicationAttachment.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.online.education.app.core.util.compose.AdaptiveDialog

@Composable
fun AddLinkDialog(
    onDismissRequest: () -> Unit,
    onLinkAdded: (link: String, name: String) -> Unit
) {
    var link by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    AdaptiveDialog(
        onDismiss = onDismissRequest
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(8.dp)
        ) {
            OutlinedTextField(
                value = link,
                onValueChange = {
                    link = it
                },
                label = {
                    Text(text = "Ссылка")
                },
                modifier = Modifier.fillMaxWidth()

            )
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                },
                label = {
                    Text(text = "Название")
                },
                modifier = Modifier.fillMaxWidth()

            )
            Button(
                onClick = {
                    onLinkAdded(link, name)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Добавить ссылку")
            }
        }
    }
}