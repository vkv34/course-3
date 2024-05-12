package ru.online.education.app.feature.publication.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.cash.paging.LoadStateLoading
import app.cash.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.FlowPreview
import ru.online.education.app.core.util.api.baseUrl
import ru.online.education.app.core.util.compose.debbugable
import ru.online.education.app.core.util.time.customFormat
import ru.online.education.app.feature.publication.domain.PublicationScreenState
import ru.online.education.app.feature.publication.model.Publication
import ru.online.education.domain.repository.model.PublicationAnswerAttachmentDto
import ru.online.education.domain.repository.model.PublicationAnswerDto
import ru.online.education.domain.repository.model.PublicationAttachmentType
import ru.online.education.domain.repository.model.PublicationCategory

@OptIn(FlowPreview::class, ExperimentalFoundationApi::class)
@Composable
fun PublicationListScreen(
    state: PublicationScreenState,
    editable: Boolean = false,
    onEditClick: (Publication) -> Unit = {},
    onDeleteClick: (Publication) -> Unit = {},
    courseName: String,
    onCourseNameChange: (String) -> Unit
) {
    val publications = state.publications
        .collectAsLazyPagingItems()
    val isLoading =
        publications.loadState.append is LoadStateLoading || publications.loadState.refresh is LoadStateLoading

    val screenState by state.screenState.collectAsState()
    val attachments by state.attachments.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = PublicationScreenState.ScreenState.Tab.entries.indexOf(screenState.tab)
        ) {
            PublicationScreenState.ScreenState.Tab.entries.forEach { tab ->
                Tab(
                    selected = tab == screenState.tab,
                    onClick = {
                        state.selectTab(tab)
                    },
                    text = {
                        Text(tab.tabName)
                    }
                )
            }
        }

        var editableCourseName by remember(courseName) { mutableStateOf(courseName) }

        if (editable) {
            OutlinedTextField(
                value = editableCourseName,
                onValueChange = {
                    editableCourseName = it
                },
                label = { Text("Изменить название курса") },
                isError = editableCourseName.isEmpty(),
                supportingText = {
                    if (editableCourseName.isEmpty()) {
                        Text("Поле обязательно к заполнению")
                    }
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            onCourseNameChange(editableCourseName)
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Check, null)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        AnimatedContent(screenState.tab) { seelctedTab ->
            when (seelctedTab) {
                PublicationScreenState.ScreenState.Tab.Publications -> LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .debbugable()
                ) {
                    items(publications.itemCount) { index: Int ->
                        val publication = publications[index]
                        if (publication != null) {
                            key(publication.id) {
                                PublicationCard(
                                    publication = publication,
                                    attachments = attachments[publication.id] ?: listOf(),
                                    collapsableContent = { expanded ->
                                        if (!editable && expanded && publication.type == PublicationCategory.WithAnswer) {
                                            StudentAnswerList(
                                                publicationOnCourseId = publication.publicationInCourseId,
                                                screenState = state,
                                                modifier = Modifier.fillMaxWidth()
                                                    .heightIn(max = 500.dp)
                                            )
                                        } else if (editable && expanded && publication.type == PublicationCategory.WithAnswer) {
                                            TeacherAnswerList(
                                                publicationOnCourseId = publication.publicationInCourseId,
                                                screenState = state,
                                                modifier = Modifier.fillMaxWidth()
                                                    .heightIn(max = 500.dp)
                                            )
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                        .animateItemPlacement(),
                                    editable = editable,
                                    onEditClick = {
                                        onEditClick(publication)
                                    },
                                    onDeleteCLick = {
                                        onDeleteClick(publication)
                                    }
                                )
                            }
                            key("spacer") {
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                    item {
                        if (isLoading) {
                            Box(modifier = Modifier.fillParentMaxWidth().debbugable()) {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center)
                                        .padding(16.dp)
                                        .debbugable()
                                )
                            }
                        }
                    }
                }

                PublicationScreenState.ScreenState.Tab.Users -> UsersList(
                    screenState = state,
                    editable = editable
                )
            }
        }

    }


}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StudentAnswerCard(
    answerDto: PublicationAnswerDto,
    attachments: List<PublicationAnswerAttachmentDto> = listOf(),
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Row {
                Text("Время ответа - ")
                Text(
                    answerDto.dateCreate.customFormat(),
                    fontWeight = FontWeight.Bold
                )
            }
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))

            Text(answerDto.answer)
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            val localUriHandler = LocalUriHandler.current
            if (attachments.isNotEmpty()) {
                Text("Вложения")
                HorizontalDivider(modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                attachments.forEach { attachment ->
                    FlowRow(
                        modifier = Modifier
                            .clickable {
                                if (attachment.contentType == PublicationAttachmentType.Link) {
                                    localUriHandler.openUri(attachment.content)
                                    return@clickable
                                }
                                localUriHandler.openUri("$baseUrl/publicationAnswer/file/${attachment.id}")

                            }
                            .padding(4.dp)

                    ) {
                        Icon(
                            imageVector = Icons.Default.AttachFile,
                            null,
                            modifier = Modifier.size(16.dp)
                                .alignByBaseline()

                        )
                        Text(
                            attachment.name,
                            modifier = Modifier
                                .alignByBaseline()
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Прикреплено: ${attachment.dateCreate.customFormat()}",
                            maxLines = 2,
                            softWrap = true,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier
                                .alignByBaseline()
                        )
                    }
                }
            }

            if (answerDto.mark != null) {
                Row {
                    Text("Оценка: ")
                    Text(answerDto.mark.toString())
                }
            } else {
                Text(
                    "Не проверенно ",
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            }
            Spacer(Modifier.height(8.dp))

            if (!answerDto.comment.isNullOrBlank()) {
                Text("Комментарий от: ${answerDto.teacher?.fio}")
                HorizontalDivider(modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                Text(answerDto.comment!!)
            }

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 3.dp,
                color = MaterialTheme.colorScheme.primary
            )

        }
    }
}