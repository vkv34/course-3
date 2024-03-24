package model

import core.Validator
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class PublicationDto(
    val publicationId: Int,
    val courseId: Int = 0,
    val publicationInCourseId: Int = 0,
    val title: String = "",
    val subTitle: String = "",
    val content: String = "",
    val authorId: Int = 0,
    @SerialName("publicationType")
    val type: PublicationCategory = PublicationCategory.WithoutAnswer,
    val visible: Boolean = true,
    val temp: Boolean = false
): BaseModel(), Validator{
    override fun validate(): Map<String, String> {
        val errors = mutableMapOf<String, String>()
        when {
            title.isEmpty() -> errors[::title.name] = "Заголовок не может быть пустым"
            subTitle.isEmpty() -> errors[::subTitle.name] = "Подзаголовок не может быть пустым"
        }
        return errors
    }

}
