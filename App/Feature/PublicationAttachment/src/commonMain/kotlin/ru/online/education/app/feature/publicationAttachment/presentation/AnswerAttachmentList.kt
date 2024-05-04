package ru.online.education.app.feature.publicationAttachment.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.mohamedrejeb.calf.core.LocalPlatformContext
import com.mohamedrejeb.calf.io.getName
import com.mohamedrejeb.calf.io.readByteArray
import com.mohamedrejeb.calf.picker.FilePickerFileType
import com.mohamedrejeb.calf.picker.FilePickerSelectionMode
import com.mohamedrejeb.calf.picker.rememberFilePickerLauncher
import ru.online.education.app.core.util.api.baseUrl
import ru.online.education.app.core.util.compose.debbugable
import ru.online.education.app.feature.publicationAttachment.domain.AnswerAttachmentListState
import ru.online.education.app.feature.publicationAttachment.domain.AttachmentListState
import ru.online.education.app.feature.publicationAttachment.domain.AttachmentState
import ru.online.education.app.feature.publicationAttachment.domain.model.Attachment
import ru.online.education.domain.repository.model.PublicationAttachmentType

@Composable
fun AnswerAttachmentList(
    attachmentListState: AnswerAttachmentListState,
    modifier: Modifier = Modifier
) {
    val attachments by attachmentListState.attachments.collectAsState()

    val scope = rememberCoroutineScope()
    val context = LocalPlatformContext.current
    val filePickerLauncher = rememberFilePickerLauncher(
        selectionMode = FilePickerSelectionMode.Multiple
    ) { files ->
        files.forEach {
            attachmentListState.uploadFile(
                file = Attachment.File(
                    name = it.getName(context) ?: "",
                    url = ""
                ),
                fileBytes = { it.readByteArray(context) },
                fileType = PublicationAttachmentType.File
            )
        }

    }
    val imagePickerLauncher = rememberFilePickerLauncher(
        selectionMode = FilePickerSelectionMode.Multiple,
        type = FilePickerFileType.Image
    ) { images ->
        images.forEach {
            attachmentListState.uploadFile(
                file = Attachment.Image(
                    name = it.getName(context) ?: "",
                    url = ""
                ),
                fileBytes = { it.readByteArray(context) },
                fileType = PublicationAttachmentType.Image
            )
        }

    }
    val uriHandler = LocalUriHandler.current

    LazyColumn(
        modifier = modifier
            .heightIn(max = 600.dp)
    ) {
        items(attachments) { attachmentCardState ->
            val attachment by attachmentCardState.attachment.collectAsState()
            key(attachment.attachment.id) {
                AttachmentCard(
                    state = attachment,
                    onClick = { clickedAttachment ->
                        if (clickedAttachment !is AttachmentState.Loaded) return@AttachmentCard
                        when (val attach = clickedAttachment.attachment) {
                            is Attachment.File -> uriHandler.openUri("${baseUrl}attachment/file/${attach.id}")
                            is Attachment.Image -> Unit
                            is Attachment.Link -> uriHandler.openUri(attach.url)
                        }
                    },
                    onDeleteCLick = { attachmentListState.removeAttachment(attachmentCardState) },
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

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    },
                    properties = PopupProperties(
                        focusable = true,
                        dismissOnBackPress = true,
                        dismissOnClickOutside = true
                    )
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
                    DropdownMenuItem(
                        text = {
                            Text("Файл")
                        },
                        onClick = {
                            filePickerLauncher.launch()
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Description, contentDescription = null)
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text("Изображение")
                        },
                        onClick = {
                            imagePickerLauncher.launch()
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Image, contentDescription = null)
                        }
                    )
                }

                if (addLinkDialoOpened) {
                    AddLinkDialog(
                        onDismissRequest = {
                            addLinkDialoOpened = false
                            expanded = false
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
}