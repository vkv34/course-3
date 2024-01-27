package model

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
}