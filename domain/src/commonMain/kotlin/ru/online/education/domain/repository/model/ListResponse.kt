package ru.online.education.domain.repository.model

import kotlinx.serialization.Serializable

@Serializable
data class  ListResponse<T: BaseModel>(
    val values: List<T>
): BaseModel()
