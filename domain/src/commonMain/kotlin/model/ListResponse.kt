package model

import kotlinx.serialization.Serializable

@Serializable
data class  ListResponse<T>(
    val values: List<T>
): BaseModel()
