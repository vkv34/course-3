package model

import kotlinx.serialization.Serializable

@Serializable
data class User (
    val id: Int = 0,
    val email: String = "",
    val password: String = "",
    val firstName: String = "",
    val secondName: String = "",
    val lastName: String = "",


    val role: UserRole = UserRole.Unspecified
): BaseModel()