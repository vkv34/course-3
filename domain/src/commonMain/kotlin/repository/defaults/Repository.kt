package repository.defaults

import model.BaseModel
import model.ListResponse
import util.ApiResult

interface Repository<T: BaseModel, K> : DefaultPaging {
    suspend fun getAll(page: Int): ApiResult<ListResponse<T>>

    suspend fun getById(id: K): ApiResult<T>

    suspend fun deleteById(id: K): ApiResult<T>

    suspend fun delete(data: T): ApiResult<T> = ApiResult.Error("Not Implemented")

    suspend fun deleteAll(vararg data: T): ApiResult<T> = ApiResult.Error("Not Implemented")

    suspend fun update(data: T): ApiResult<T?>

    suspend fun add(data: T): ApiResult<T>

    suspend fun addAll(vararg data: T): ApiResult<List<T>> = ApiResult.Error("Not Implemented")
}