package presentation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.SyncProblem
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import domain.AttachmentState
import domain.model.Attachment
import ru.online.education.app.core.util.compose.debbugable

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
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val attachment = state.attachment
    Card(
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .wrapContentSize()
                .debbugable(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
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
                    Crossfade(
                        targetState = state,
                        modifier = Modifier.fillMaxSize()
                            .debbugable()
                    ) { attachmentState ->
                        when (attachmentState) {
                            is AttachmentState.Loaded -> {}
                            is AttachmentState.Loading -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(color = Color.Black.copy(alpha = 0.5f))
                                        .debbugable(),
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(8.dp)
                                            .align(Alignment.Center)
                                            .debbugable()
                                    )
                                }
                            }

                            is AttachmentState.Error -> {
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
        }
    }

}