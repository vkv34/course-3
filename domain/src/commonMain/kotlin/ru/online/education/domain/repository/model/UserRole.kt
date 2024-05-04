package ru.online.education.domain.repository.model

import kotlinx.serialization.Serializable

@Serializable
enum class UserRole {
    Admin,
    Moderator,
    Teacher,
    Student,
    Unspecified;


    companion object {
        val all = listOf(
            Admin,
            Moderator,
            Teacher,
            Student,
            Unspecified
        )
    }

    override fun toString(): String = when(this){
        Admin -> "Преподаватель"
        Moderator -> "TODO()"
        Teacher -> "Преподователь"
        Student -> "Студент"
        Unspecified -> "TODO()"
    }
}