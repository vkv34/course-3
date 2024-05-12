package ru.online.education.app.feature.publication.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import app.cash.paging.LoadStateLoading
import app.cash.paging.compose.collectAsLazyPagingItems
import ru.online.education.app.core.util.compose.debbugable
import ru.online.education.app.feature.publication.domain.PublicationScreenState
import ru.online.education.app.feature.publicationAttachment.presentation.AnswerAttachmentList
import ru.online.education.domain.repository.model.UserDto

@OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
fun TeacherAnswerList(
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
    val attachments by screenState.answers.collectAsState()
    var userToAnswer by remember { mutableStateOf(mapOf<UserDto, List<Int>>()) }
    var expandedState by remember { mutableStateOf(mapOf<UserDto, Boolean>()) }
    LaunchedEffect(answers.itemCount) {
        if (answers.itemCount == 0) {
            userToAnswer = mapOf()
//            expandedState = mapOf()
            screenState.clearAnswers()
        }
    }
    Column {
        LazyColumn(
            modifier = modifier
        ) {
            repeat(answers.itemCount) { index ->
                val answer = try {
                    answers[index]
                } catch (e: Exception) {
                    null
                }
                if (answer?.userDto != null) {
                    userToAnswer +=
                        answer.userDto!! to ((userToAnswer[answer.userDto] ?: listOf()).let {
                            if (index !in it) {
                                it + index
                            } else {
                                it
                            }
                        })
//                    expandedState += answer.userDto!! to false
                }
            }
            userToAnswer.forEach { entry ->
                stickyHeader(
                    key = entry.key.id.toString()+"student"
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                            .padding(start = 16.dp, bottom = 8.dp, end = 16.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = if (expandedState[entry.key] == true)
                                4.dp
                            else
                                0.dp,
                            focusedElevation = if (expandedState[entry.key] == true)
                                4.dp
                            else
                                0.dp
                        ),
                        onClick = {
                            val newEntry = entry.key to !(expandedState[entry.key] ?: false)
                            expandedState += newEntry
                        },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = contentColorFor(MaterialTheme.colorScheme.tertiaryContainer)
                        )
                    ) {
                        FlowRow(
                            modifier = Modifier
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = entry.key.fio,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )

                            Icon(
                                imageVector = if (expandedState[entry.key] == false)
                                    Icons.Default.ArrowDropDown
                                else Icons.Default.ArrowDropUp,
                                null,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                    }
                }

                if (expandedState[entry.key] == true) {
                    entry.value.forEach { answerIndex ->
                        val answer = try {
                            answers[answerIndex]
                        } catch (e: Exception) {
                            null
                        }
                        if (answer != null) {
                            item(
                                key = answer.id
                            ) {
                                LaunchedEffect(Unit) {
                                    screenState.loadAnswerAttachments(answerId = answer.id)
                                }
                                Column(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    StudentAnswerCard(
                                        answerDto = answer,
                                        attachments = attachments[answer.id] ?: listOf(),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    if (answer.mark == null) {
                                        val allowMarks = remember { arrayOf(null, 1, 2, 3, 4, 5) }
                                        var selectedMark by remember { mutableStateOf<Int?>(null) }
                                        var expanded by remember { mutableStateOf(false) }
                                        var comment by remember { mutableStateOf("") }
                                        FlowRow {
                                            Row(
                                                modifier = Modifier.align(Alignment.CenterVertically)
                                            ) {
                                                Text(
                                                    text = "Оценка",
                                                    style = MaterialTheme.typography.bodyLarge
                                                )
                                                Text(
                                                    text = selectedMark?.toString() ?: "Не указана",
                                                    modifier = Modifier.clickable {
                                                        expanded = !expanded
                                                    }
                                                        .clip(MaterialTheme.shapes.small)
                                                        .background(MaterialTheme.colorScheme.onTertiary)
                                                        .padding(4.dp),
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    color = contentColorFor(MaterialTheme.colorScheme.onTertiary)
                                                )
                                                DropdownMenu(
                                                    expanded = expanded,
                                                    onDismissRequest = {
                                                        expanded = false
                                                    }
                                                ) {
                                                    allowMarks.forEach { mark ->
                                                        DropdownMenuItem(
                                                            text = {
                                                                Text(text = mark?.toString() ?: "Не указана")
                                                            },
                                                            onClick = {
                                                                selectedMark = mark
                                                            }
                                                        )
                                                        HorizontalDivider()
                                                    }
                                                }
                                            }
                                            OutlinedTextField(
                                                value = comment,
                                                onValueChange = {
                                                    comment = it
                                                },
                                                label = {
                                                    Text("Комментарий")
                                                },
                                            )
                                            TextButton(
                                                onClick = {
                                                    answerListState.sendMarkAndComment(
                                                        mark = selectedMark?.toByte(),
                                                        comment = comment,
                                                        answerId = answer.id
                                                    )
                                                }
                                            ) {
                                                Text("Отправить оценку")
                                            }
                                        }
                                    } else {
                                        TextButton(
                                            onClick = {
                                                answerListState.sendMarkAndComment(
                                                    mark = null,
                                                    comment = null,
                                                    answerId = answer.id
                                                )
                                            }
                                        ) {
                                            Text("Изменить оценку")
                                        }
                                    }
                                }
                            }
                        }
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
        }
//        var answer by remember {
//            mutableStateOf("")
//        }
//        val answerAttachmentState = remember { screenState.createAnswerAttachmentListState() }
//
//
//        Column(
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            AnswerAttachmentList(
//                attachmentListState = answerAttachmentState,
//                modifier = Modifier.fillMaxWidth()
//                    .heightIn(max = 500.dp)
//            )
//
//            Row {
//                TextField(answer, onValueChange = { answer = it })
//                Button(
//                    onClick = {
//                        answerListState.addAnswer(
//                            answer,
//                            onAdd = { answerId ->
//                                answerAttachmentState.sendAll(answerId)
//                            }
//                        )
//                    }
//                ) {
//                    Text("Добавить ответ")
//                }
//            }
//        }
    }

}

