package ru.online.education.domain.model

import model.ListResponse
import util.ApiResult

interface BaseService<T, K> {
    suspend fun create(data: T): ApiResult<K>

    suspend fun update(data: T): ApiResult<K>

    suspend fun getAll(page: Int): ApiResult<ListResponse<T>>

    suspend fun getById(id: Int): ApiResult<T>

    suspend fun deleteById(id: Int): ApiResult<K>
}