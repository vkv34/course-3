package ru.online.education.app.feature.publicationAttachment.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import ru.online.education.app.core.util.api.baseUrl
import ru.online.education.app.core.util.compose.ConfirmDialogCard
import ru.online.education.app.core.util.compose.debbugable
import ru.online.education.app.feature.publicationAttachment.domain.AttachmentState
import ru.online.education.app.feature.publicationAttachment.domain.LoadState
import ru.online.education.app.feature.publicationAttachment.domain.model.Attachment

val Attachment.icon: ImageVector
    get() = when (this) {
        is Attachment.File -> Icons.Default.Description
        is Attachment.Image -> Icons.Default.Image
        is Attachment.Link -> Icons.Default.Link
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttachmentCard(
    state: AttachmentState,
    onClick: (AttachmentState) -> Unit,
    onDeleteCLick: (AttachmentState) -> Unit,
    modifier: Modifier = Modifier
) {
    val attachment = state.attachment
    Card(
        onClick = {
            onClick(state)
        },
        modifier = modifier
    ) {
        Box(
            modifier = modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .debbugable(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                if (state is AttachmentState.Loading || state is AttachmentState.Error || attachment !is Attachment.Image) {
                    Box(
                        modifier = Modifier
                            .size(75.dp)
                            .debbugable()
                    ) {
                        Icon(
                            imageVector = attachment.icon,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                                .debbugable()
                        )

                        TooltipBox(
                            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(4.dp),
                            state = rememberTooltipState(),
                            modifier = Modifier.fillMaxSize()
                                .debbugable(),
                            tooltip = {
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                                    )

                                ) {
                                    Text(
                                        text = when (state) {
                                            is AttachmentState.Error -> state.message.ifBlank { "Ошибка" }
                                            is AttachmentState.Loaded -> state.attachment.displayName
                                            is AttachmentState.Loading -> "${(state.progress * 100).toInt()} %"
                                        },
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }
                        ) {
                            val loadState by remember(state) {
                                mutableStateOf(
                                    when (state) {
                                        is AttachmentState.Error -> LoadState.ERROR
                                        is AttachmentState.Loaded -> LoadState.SUCCESS
                                        is AttachmentState.Loading -> LoadState.LOADING
                                    }
                                )
                            }
                            val progress by animateFloatAsState((state as? AttachmentState.Loading)?.progress ?: 0f)

                            Crossfade(
                                targetState = loadState,
                                modifier = Modifier.fillMaxSize()
                                    .debbugable()
                            ) { attachmentState ->
                                when (attachmentState) {
                                    LoadState.SUCCESS -> {}
                                    LoadState.LOADING -> {

                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(color = Color.Black.copy(alpha = 0.5f))
                                                .debbugable(),
                                        ) {
                                            CircularProgressIndicator(
                                                progress = { progress },
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .padding(8.dp)
                                                    .align(Alignment.Center)
                                                    .debbugable(),
                                            )
                                        }
                                    }

                                    LoadState.ERROR -> {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f))
                                                .debbugable(),
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.SyncProblem,
                                                tint = MaterialTheme.colorScheme.onErrorContainer,
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .padding(8.dp)
                                                    .align(Alignment.Center)
                                                    .debbugable()
                                            )
                                        }
                                    }
                                }
                            }

                        }
                    }


                    Spacer(Modifier.width(8.dp))
                    Column(
                        modifier = Modifier
                            .wrapContentSize()
                            .debbugable(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            attachment.name,
                            maxLines = 1,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.debbugable()
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            attachment.displayName,
                            maxLines = 1,
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.debbugable()
                        )
                    }

                } else {
                    var error by remember { mutableStateOf("") }
                    val loader = remember { ImageLoader(PlatformContext.INSTANCE) }
                    AsyncImage(
                        model = "${baseUrl}attachment/file/${attachment.id}",
                        contentDescription = null,
                        imageLoader = loader,
                        modifier = Modifier.fillMaxSize(),
                        onState = {
                            if (it is AsyncImagePainter.State.Error) {
                                error = it.result.throwable.message ?: ""
                            }
                        }
                    )
                    if (error.isNotBlank()) {
                        Dialog(
                            onDismissRequest = {
                                error = ""
                            }
                        ) {

                            Text(error)
                        }
                    }
//                    Image(
//                        painter = coil3.compose.rememberAsyncImagePainter(model = attachment.url),
//                        contentDescription = null,
//                        modifier = Modifier
//                            .size(75.dp)
//                            .debbugable()
//                    )
                }

            }
            if (state is AttachmentState.Loaded) {
                var confirmOpened by remember { mutableStateOf(false) }
                IconButton(
                    onClick = {
                        confirmOpened = !confirmOpened
                    },
                    modifier = Modifier.align(Alignment.CenterEnd)
                        .padding(end = 8.dp)

                ) {
                    Icon(
                        imageVector = Icons.Default.DoDisturbOn,
                        contentDescription = null
                    )

                    if (confirmOpened) {
                        Popup(
                            onDismissRequest = { confirmOpened = false },
                        ) {
                            ConfirmDialogCard(
                                title = "Удалить",
                                subTitle = "Вы действительно хотите удалить этот файл?",
                                onConfirm = {
                                    onDeleteCLick(state)
                                },
                                onDismiss = {
                                    confirmOpened = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }

}