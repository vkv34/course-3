package presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import model.Publication
import ru.online.education.app.core.util.compose.debbugable
import ru.online.education.app.core.util.time.customFormat

@Composable
fun PublicationCard(
    publication: Publication,
    collapsableContent: @Composable (expanded: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    editable: Boolean = false,
    onEditClick: () -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = modifier
            .padding(8.dp)
//            .animateContentSize()
            .debbugable(),

        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(8.dp)
                .debbugable()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .debbugable()
                    .animateContentSize()
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                        .clickable(
                            role = Role.DropdownList,
                            interactionSource = MutableInteractionSource(),
                            indication = null
                        ) {
                            expanded = !expanded
                        }
                        .debbugable()
                ) {
                    Column(
                        modifier = Modifier
                            .debbugable()
                    ) {
                        Text(
                            publication.title,
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier.debbugable()
                        )
                        Spacer(
                            Modifier.height(8.dp)
                                .debbugable()
                        )
                        Text(
                            text = publication.subTitle,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.debbugable()
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                    ) {

                        Column {
                            if (publication.temp) {
                                Text(
                                    text = "Черновик",
                                    modifier = Modifier.debbugable(),
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                            if (!publication.visible) {
                                Text(
                                    text = "Не опубликован",
                                    modifier = Modifier.debbugable(),
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                        Text(
                            text = publication.createdAt?.customFormat() ?: "",
                            modifier = Modifier.debbugable(),
                            style = MaterialTheme.typography.titleSmall
                        )
                        val rotation by animateFloatAsState(if (expanded) 180f else 0f)
                        IconButton(
                            onClick = {
                                expanded = !expanded
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                modifier = Modifier
                                    .rotate(rotation)
                                    .debbugable(),
                            )
                        }

                       if (editable) {
                           IconButton(
                               onClick = onEditClick
                           ) {
                               Icon(
                                   imageVector = Icons.Default.Edit,
                                   contentDescription = null
                               )
                           }
                       }
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth()
                        .debbugable()
                )

                if (expanded && publication.content.isNotEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                            .padding(8.dp)
                            .debbugable()
                    ) {
                        Spacer(
                            Modifier.height(16.dp)
                                .debbugable()
                        )
                        val richTextState = rememberRichTextState()

                        LaunchedEffect(Unit) {
                            richTextState.setHtml(publication.content)
                        }

                        SelectionContainer(
                            Modifier.fillMaxWidth()
                                .debbugable()
                        ) {
                            RichText(
                                state = richTextState,
                                modifier = Modifier.fillMaxWidth()
                                    .debbugable(),
                                style = MaterialTheme.typography.bodyLarge
                            )

                        }


                    }
                }
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth()
                        .debbugable()
                )

                collapsableContent(expanded)

            }
        }
    }
}

