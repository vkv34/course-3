package util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
sealed class ApiResult<T>(
//    val code: Int,
) {
    abstract val message: String

    @Serializable
    class Success<T>(val data: T, override val message: String = "") : ApiResult<T>()

    @Serializable
    class Error<T>(
        override val message: String,
        @Serializable(with = ThrowableSerializer::class) val throwable: Throwable? = null
    ) :
        ApiResult<T>() {
        companion object {
            inline fun <reified T> notImplemented() = Error<T>("Not implemented", NotImplementedError())
        }
    }


    @Serializable
    class Empty<T>(override val message: String) : ApiResult<T>()

    fun successOrNull() = (this as? Success<T>)?.data
}

object ThrowableSerializer : KSerializer<Throwable> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Throwable", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Throwable = Throwable(decoder.decodeString())
    override fun serialize(encoder: Encoder, value: Throwable) {
        encoder.encodeString(value.message ?: "")
    }


}

inline fun <reified T, reified R> ApiResult<T>.map(mapper: (T) -> R): ApiResult<R> = when (this) {
    is ApiResult.Success -> ApiResult.Success(mapper(data), message)
    is ApiResult.Error -> ApiResult.Error(message, throwable)
    is ApiResult.Empty -> ApiResult.Empty(message)
}
