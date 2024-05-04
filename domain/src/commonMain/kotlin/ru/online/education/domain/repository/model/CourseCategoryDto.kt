package ru.online.education.domain.repository.model

import kotlinx.serialization.Serializable
import ru.online.education.domain.core.Validator

@Serializable
data class CourseCategoryDto(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
) : BaseModel(), Validator {


    override fun validate(): Map<String, String> {
        val errors = mutableMapOf<String, String>()
        when {
            name.isBlank() -> errors[::name.name] = "Наименование не может быть пустым"
        }
        return errors
    }
}