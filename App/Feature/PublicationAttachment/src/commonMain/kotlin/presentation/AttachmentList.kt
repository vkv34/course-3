package presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import domain.AttachmentListState
import domain.model.Attachment
import ru.online.education.app.core.util.compose.debbugable

@Composable
fun AttachmentList(
    attachmentListState: AttachmentListState,
    modifier: Modifier = Modifier
) {
    val attachments by attachmentListState.attachments.collectAsState()

    LazyColumn(
        modifier = modifier
            .heightIn(max = 600.dp)
    ) {
        items(attachments) { attachmentCardState ->
            val attachment by attachmentCardState.attachment.collectAsState()
            key(attachment.attachment.id) {
                AttachmentCard(
                    state = attachment,
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                        .debbugable()
                )
            }
        }

        item("addButton") {
            var expanded by remember { mutableStateOf(false) }
            var addLinkDialoOpened by remember { mutableStateOf(false) }
            OutlinedButton(
                onClick = {
                    expanded = !expanded
                }
            ) {
                Text("Добавить вложение")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                DropdownMenuItem(
                    text = {
                        Text("Ссылка")
                    },
                    onClick = {
                        addLinkDialoOpened = true
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Link, contentDescription = null)
                    }
                )
            }

            if (addLinkDialoOpened) {
                AddLinkDialog(
                    onDismissRequest = {
                        addLinkDialoOpened = false
                    },
                    onLinkAdded = { link, name ->
                        attachmentListState.addAttachment(
                            Attachment.Link(
                                id = 0,
                                name = name,
                                url = link
                            )
                        )
                        addLinkDialoOpened = false
                    }
                )
            }
        }
    }
}