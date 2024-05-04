package ru.online.education.app.feature.publication.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import app.cash.paging.LoadStateLoading
import app.cash.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.FlowPreview
import ru.online.education.app.core.util.compose.debbugable
import ru.online.education.app.core.util.time.customFormat
import ru.online.education.app.feature.publication.domain.PublicationScreenState
import ru.online.education.app.feature.publication.model.Publication
import ru.online.education.app.feature.publicationAttachment.domain.AttachmentState
import ru.online.education.app.feature.publicationAttachment.domain.mapper.toAttachment
import ru.online.education.app.feature.publicationAttachment.domain.model.Attachment
import ru.online.education.app.feature.publicationAttachment.presentation.AttachmentCard
import ru.online.education.domain.repository.model.PublicationAnswerAttachmentDto
import ru.online.education.domain.repository.model.PublicationAnswerDto
import ru.online.education.domain.repository.model.PublicationCategory

@OptIn(FlowPreview::class, ExperimentalFoundationApi::class)
@Composable
fun PublicationListScreen(
    state: PublicationScreenState,
    editable: Boolean = false,
    onEditClick: (Publication) -> Unit = {},
    onDeleteClick: (Publication) -> Unit = {}
) {
    val publications = state.publications
        .collectAsLazyPagingItems()
    val isLoading =
        publications.loadState.append is LoadStateLoading || publications.loadState.refresh is LoadStateLoading

    val screenState by state.screenState.collectAsState()

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
                                    collapsableContent = { expanded ->
                                        if (!editable && expanded && publication.type == PublicationCategory.WithAnswer){
                                            StudentAnswerList(
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
                .padding(8.dp)
        ) {
            Row {
                Text("Ответ от ")
                Text(
                    answerDto.dateCreate.customFormat(),
                    fontWeight = FontWeight.Bold
                )
            }
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))

            Text(answerDto.answer)

            if (attachments.isNotEmpty()){
                Text("Вложения")
                HorizontalDivider(modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                attachments.forEach { attachment ->
                    AttachmentCard(
                        state = AttachmentState.Loaded(attachment = attachment.toAttachment()),
                        onClick = {},
                        onDeleteCLick = {},
                    )
                    Spacer(Modifier.height(8.dp))
                }
            }

            if (answerDto.mark != null){
                Row {
                    Text("Оценка: ")
                    Text(answerDto.mark.toString())
                }
            }else{
                Text(
                    "Не проверенно ",
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            }
            Spacer(Modifier.height(8.dp))

            if (answerDto.comment != null){
                Text("Комментарий от: ${answerDto.teacher?.fio}")
                HorizontalDivider(modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
               Text(answerDto.comment!!)
            }
        }
    }
}