package domain

import domain.mapper.toPublication
import domain.mapper.toPublicationDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.Publication
import model.PublicationCategory
import model.PublicationDto
import repository.PublicationOnCourseRepository
import repository.PublicationRepository
import repository.UserRepository
import util.ApiResult

class AddPublicationScreenState(
//    private val publicationOnCourseRepository: PublicationOnCourseRepository,
    private val publicationRepository: PublicationRepository,
    private val userRepository: UserRepository,
    private val coroutineScope: CoroutineScope,
    private val courseId: Int,
    val onDismiss: () -> Unit,
) {

    data class ScreenState(
        val error: String = "",
        val saveState: SaveState = SaveState.NotSaved
    ) {
        enum class SaveState {
            NotSaved,
            Saving,
            Saved,
            Error
        }
    }


    private val _publication = MutableStateFlow(Publication())
    val publication = _publication.asStateFlow()

    private val _uiState = MutableStateFlow(ScreenState())
    val uiState = _uiState.asStateFlow()

    fun setPublicationTitle(title: String) {
        _publication.update { it.copy(title = title) }
    }

    fun setPublicationSubtitle(subtitle: String) {
        _publication.update { it.copy(subTitle = subtitle) }
    }

    fun setPublicationContent(content: String) {
        _publication.update { it.copy(content = content) }
    }

    fun setPublicationCategory(category: PublicationCategory) {
        _publication.update { it.copy(type = category) }
    }

    fun publish() {
        setLoadState(ScreenState.SaveState.Saving)
        coroutineScope.launch {
            val publication = _publication.value.copy(
                visible = true,
                temp = false
            )

            val result = addOrUpdatePublication(publication.toPublicationDto())

            if (result is ApiResult.Success) {
                setLoadState(ScreenState.SaveState.Saved)
                onDismiss()
            } else {
                setLoadState(ScreenState.SaveState.Error)
                _uiState.update { it.copy(error = result.message) }
            }
        }
    }

    private fun setLoadState(loadState: ScreenState.SaveState) = _uiState.update { it.copy(saveState = loadState) }

    fun setPublicationId(publicationId: Int) {

        coroutineScope.launch {
            val publicationApiResult = publicationRepository.getById(publicationId)
            if (publicationApiResult is ApiResult.Success) {
                _publication.value = publicationApiResult.data.toPublication(author = "", category = "")

                _publication.update { it.copy(author = getAuthor(publicationApiResult.data.authorId)) }

            }

        }
    }

    suspend fun getCategory(): String {
        return ""
    }

    suspend fun getAuthor(authorId: Int): String =
        when (val result = userRepository.getById(authorId)) {
            is ApiResult.Success -> result.data.fio
            else -> result.message
        }

    suspend fun addOrUpdatePublication(publication: PublicationDto) =
        publicationRepository
        .add(
            publication.copy(
                courseId = courseId
            )
        )
    fun saveClick() {
        TODO("Not yet implemented")
    }
}