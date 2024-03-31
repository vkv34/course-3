package domain

import domain.mapper.toPublication
import domain.mapper.toPublicationDto
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import model.Publication
import model.PublicationCategory
import model.PublicationDto
import repository.PublicationRepository
import repository.UserRepository
import ru.online.education.app.core.util.coruotines.DispatcherProvider
import util.ApiResult

class AddPublicationScreenState(
//    private val publicationOnCourseRepository: PublicationOnCourseRepository,
    private val publicationRepository: PublicationRepository,
    private val userRepository: UserRepository,
    coroutineScope: CoroutineScope,
    private val courseId: Int,
    val onDismiss: () -> Unit,
) {
    private val screenJob = Job()
    private val coroutineScope = coroutineScope.plus(screenJob)

    private var currentPublicationOnCourseId = 0

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


    init {
        saveTemp()
    }

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
                coroutineScope.cancel()
                screenJob.complete()
            } else {
                setLoadState(ScreenState.SaveState.Error)
                _uiState.update { it.copy(error = result.message) }
            }
        }
    }


    private fun setLoadState(loadState: ScreenState.SaveState) = _uiState.update { it.copy(saveState = loadState) }

    fun setPublicationId(publicationId: Int) {

        currentPublicationOnCourseId = publicationId

        coroutineScope.launch {
            val publicationApiResult = publicationRepository.getByPublicationOnCourseId(publicationId)
            if (publicationApiResult is ApiResult.Success) {
                _publication.value = publicationApiResult.data.toPublication(author = "", category = "")

                _publication.update { it.copy(author = getAuthor(publicationApiResult.data.authorId)) }

            }else{
                _uiState.update { it.copy(error = publicationApiResult.message) }
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
                    courseId = courseId,
                )
            )

    @OptIn(FlowPreview::class)
    fun saveTemp() {
//        coroutineScope.launch(DispatcherProvider.IO) {
//            _publication
//                .debounce(5.seconds)
//                .distinctUntilChanged()
//                .collectLatest { currentPublication ->
//                    saveClick(currentPublication)
//                }
//        }
    }

    fun saveClick(publication: Publication = _publication.value) {
        coroutineScope.launch(DispatcherProvider.IO) {
            setLoadState(ScreenState.SaveState.Saving)
            val result = addOrUpdatePublication(
                publication
                    .toPublicationDto()
                    .copy(
                        courseId = courseId,
                        temp = true,
                        visible = false
                    )
            )
            if (result is ApiResult.Success) {
                setLoadState(ScreenState.SaveState.Saved)
                _publication.update {
                    result.data.toPublication(
                        author = it.author
                    )
                }
            } else {
                setLoadState(ScreenState.SaveState.Error)
                _uiState.update { it.copy(error = result.message) }
            }
        }
    }
}