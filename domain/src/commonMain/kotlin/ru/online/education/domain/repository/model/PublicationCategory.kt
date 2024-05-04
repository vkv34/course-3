package ru.online.education.domain.repository.model

enum class PublicationCategory {
    WithAnswer,
    WithoutAnswer;

    override fun toString(): String = when (this) {
        WithAnswer -> "С ответом"
        WithoutAnswer -> "Без ответа"
    }
}