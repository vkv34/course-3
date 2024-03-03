package model

import core.Validator
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class CourseDto(
    val id: Int = 0,
    val name: String = "",
    val creatorId: Int? = null,
    val shortDescription: String = "",
    val longDescription: String = "",
    val dateCreate: LocalDateTime = LocalDateTime(2024, 1, 1, 1, 1),
    val avatar: Image = Image.default,
    val background: Image = Image.default,
    val courseCategoryId: Int = 0,
) : BaseModel(), Validator {
    override fun validate(): Map<String, String> {
        val errors = mutableMapOf<String, String>()
        when {
            name.isEmpty() -> errors[::name.name] = "Наименование не может быть пустым"
        }
        return errors
    }
}
