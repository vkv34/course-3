package ru.online.education.domain.repository.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import ru.online.education.domain.core.Validator

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
//    val courseState: CourseState
) : BaseModel(), Validator {
    override fun validate(): Map<String, String> {
        val errors = mutableMapOf<String, String>()
        when {
            name.isEmpty() -> errors[::name.name] = "Наименование не может быть пустым"
            courseCategoryId == 0 -> errors[::courseCategoryId.name] = "Необходимо указать катиегорию курса"
        }
        return errors
    }
}
