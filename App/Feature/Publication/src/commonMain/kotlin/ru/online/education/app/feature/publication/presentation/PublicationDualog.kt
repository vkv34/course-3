package ru.online.education.app.feature.publication.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.SyncProblem
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.OutlinedRichTextEditor
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import ru.online.education.app.core.util.compose.AdaptiveDialog
import ru.online.education.app.core.util.compose.debbugable
import ru.online.education.app.feature.publication.domain.AddPublicationScreenState
import ru.online.education.app.feature.publication.presentation.components.RichTextStyleRow
import ru.online.education.app.feature.publicationAttachment.domain.AttachmentListState
import ru.online.education.app.feature.publicationAttachment.presentation.AttachmentList
import ru.online.education.domain.repository.model.PublicationCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPublicationDialog(
    publicationState: AddPublicationScreenState,
    attachmentListState: AttachmentListState,
    onDismiss: () -> Unit
) {
    val screenState by publicationState.uiState.collectAsState()
    val publication by publicationState.publication.collectAsState()
    val richTextState = rememberRichTextState()

    LaunchedEffect(publication.content) {
        richTextState.setHtml(publication.content)
    }

    LaunchedEffect(richTextState) {
        publicationState.setPublicationContent(richTextState.toHtml())
    }

    AdaptiveDialog(
        onDismiss = onDismiss,
        modifier = Modifier.fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .debbugable()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .debbugable(),
                verticalAlignment = CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Изменение публикации",
                    modifier = Modifier
                        .debbugable(),
                    maxLines = 1,
                    style = MaterialTheme.typography.headlineMedium
                )

                Row(
                    modifier = Modifier.debbugable(),
                    verticalAlignment = CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = publicationState::saveClick,
                        modifier = Modifier.debbugable()
                    ) {
                        Crossfade(screenState.saveState) { saveState ->
                            when (saveState) {
                                AddPublicationScreenState.ScreenState.SaveState.NotSaved ->
                                    Icon(imageVector = Icons.Default.Save, contentDescription = null)

                                AddPublicationScreenState.ScreenState.SaveState.Saving ->
                                    CircularProgressIndicator()

                                AddPublicationScreenState.ScreenState.SaveState.Saved ->
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null
                                    )

                                AddPublicationScreenState.ScreenState.SaveState.Error ->
                                    Icon(
                                        imageVector = Icons.Default.SyncProblem,
                                        contentDescription = null
                                    )
                            }
                        }
                    }

                    IconButton(
                        modifier = Modifier.debbugable(),
                        onClick = onDismiss
                    ) {
                        Icon(imageVector = Icons.Default.Cancel, contentDescription = null)
                    }

                }
            }
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth()
                    .debbugable(),
                verticalAlignment = CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text("Тип публикации")
                Spacer(modifier = Modifier.width(8.dp))
                SingleChoiceSegmentedButtonRow {
                    PublicationCategory.entries.forEach { category ->
                        SegmentedButton(
                            selected = publication.type == category,
                            onClick = {
                                publicationState.setPublicationCategory(category)
                            },
                            label = { Text(text = category.toString()) },
                            shape = RoundedCornerShape(8)
                        )
                    }
                }
            }
            AnimatedVisibility(
                visible = publication.type == PublicationCategory.WithAnswer,
            ) {
                val datePickerState = rememberDatePickerState(
                    initialSelectedDateMillis = publication
                        .deadLine
                        ?.toInstant(TimeZone.currentSystemDefault())
                        ?.epochSeconds
                        ?.times(1000),
                    selectableDates = object : SelectableDates {

                        override fun isSelectableDate(utcTimeMillis: Long): Boolean =
                            Clock.System.now().epochSeconds.times(1000) < utcTimeMillis
                    }
                )
                val timePickerState = rememberTimePickerState(
                    initialHour = publication.deadLine?.hour ?: 0,
                    initialMinute = publication.deadLine?.minute ?: 0
                )
                LaunchedEffect(datePickerState.selectedDateMillis, timePickerState.hour, timePickerState.minute) {
                    publicationState.setDeadline(
                        date = datePickerState.selectedDateMillis,
                        hour = timePickerState.hour,
                        minute = timePickerState.minute
                    )
                }

//                LaunchedEffect(Unit) {
//                    datePickerState.selectedDateMillis = publication
//                        .deadLine
//                        ?.toInstant(TimeZone.currentSystemDefault())
//                        ?.epochSeconds
//                    datePickerState.displayedMonthMillis = publication
//                        .deadLine
//                        ?.toInstant(TimeZone.currentSystemDefault())
//                        ?.epochSeconds
//                        ?: Clock.System.now().epochSeconds
//                }
                Column {
                    Text(
                        text = "Время публикации",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(8.dp))
                    DatePicker(
                        state = datePickerState,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    TimePicker(
                        state = timePickerState,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = publication.title,
                onValueChange = publicationState::setPublicationTitle,
                modifier = Modifier
                    .fillMaxWidth(),
                label = { Text(text = "Заголовок") },
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = publication.subTitle,
                onValueChange = publicationState::setPublicationSubtitle,
                modifier = Modifier
                    .fillMaxWidth(),
                label = { Text(text = "Подзаголовок") },
            )
            Spacer(Modifier.height(8.dp))

            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .padding(8.dp)
                ) {
                    RichTextStyleRow(
                        state = richTextState,
                        modifier = Modifier.fillMaxWidth()
                    )
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )
                    OutlinedRichTextEditor(
                        state = richTextState,
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        placeholder = {
                            Text("Текст к публикации")
                        }
                    )
                }
            }


//            OutlinedTextField(
//                value = publication.title,
//                onValueChange = publicationState::setPublicationTitle,
//                modifier = Modifier
//                    .fillMaxWidth(),
//                label = { Text(text = "Заголовок") },
//            )
//            Spacer(Modifier.height(8.dp))
            Text(
                text = screenState.error,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            AttachmentList(
                attachmentListState = attachmentListState,
                modifier = Modifier.fillMaxWidth()
            )

            ElevatedButton(
                onClick = {
                    publicationState.setPublicationContent(richTextState.toHtml())
                    publicationState.publish()
                }
            ) {
                Text("Опубликовать")
            }

        }
    }

}