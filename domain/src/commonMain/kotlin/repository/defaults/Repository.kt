package repository.defaults

interface Repository<T, K> : DefaultPaging {
    suspend fun getAll(page: Int): List<T>

    suspend fun getById(id: K): T?

    suspend fun deleteById(id: K): Unit

    suspend fun delete(data: T): Unit {}

    suspend fun deleteAll(vararg data: T){}

    suspend fun update(data: T): T?

    suspend fun add(data: T): K?

    suspend fun addAll(vararg data: T): Unit {}
}