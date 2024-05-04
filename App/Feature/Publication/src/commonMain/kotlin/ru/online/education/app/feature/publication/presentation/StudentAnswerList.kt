package ru.online.education.app.feature.publication.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paging.LoadStateLoading
import app.cash.paging.compose.collectAsLazyPagingItems
import ru.online.education.app.core.util.compose.debbugable
import ru.online.education.app.feature.publication.domain.PublicationScreenState
import ru.online.education.app.feature.publicationAttachment.presentation.AnswerAttachmentList

@Composable
fun StudentAnswerList(
    publicationOnCourseId: Int,
    screenState: PublicationScreenState,
    modifier: Modifier = Modifier
) {
    val answerListState = remember {
        screenState.getPublicationAnswerState(publicationOnCourseId = publicationOnCourseId)
    }

    val answers = answerListState.answers.collectAsLazyPagingItems()
    val isLoading =
        answers.loadState.append is LoadStateLoading || answers.loadState.refresh is LoadStateLoading

    LazyColumn(
        modifier = modifier
    ) {
        repeat(answers.itemCount) { index ->
            val answer = answers[index]
            if (answer != null) {
                item {
                    StudentAnswerCard(
                        answerDto = answer,
                        attachments = listOf(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        item {
            if (isLoading) {
                Box(modifier = Modifier.fillParentMaxWidth().debbugable()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                            .padding(18.dp)
                            .debbugable()
                    )
                }
            }
        }

        item {
            var answer by remember {
                mutableStateOf("")
            }


            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                val answerAttachmentState = screenState.createAnswerAttachmentListState()
                AnswerAttachmentList(
                    attachmentListState = answerAttachmentState,
                    modifier = Modifier.fillMaxWidth()
                        .heightIn(max = 500.dp)
                )

                Row {
                    TextField(answer, onValueChange = { answer = it })
                    Button(
                        onClick = {
                            answerListState.addAnswer(
                                answer,
                                onAdd = { answerId ->
                                    answerAttachmentState.sendAll(answerId)
                                }
                            )
                        }
                    ){
                        Text("Добавить ответ")
                    }
                }
            }
        }



    }

}