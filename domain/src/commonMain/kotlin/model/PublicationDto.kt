package model

import core.Validator
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
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
    val temp: Boolean = false,
    val courseCategoryId: Int = 0,

    val createdAt: LocalDateTime? = Clock.System.now().toLocalDateTime(TimeZone.UTC),
    val deadLine: LocalDateTime? = null
) : BaseModel(), Validator {
    override fun validate(): Map<String, String> {
        val errors = mutableMapOf<String, String>()
        when {
            title.isEmpty() -> errors[::title.name] = "Заголовок не может быть пустым"
            subTitle.isEmpty() -> errors[::subTitle.name] = "Подзаголовок не может быть пустым"
        }
        return errors
    }

}
