package model

import kotlinx.serialization.Serializable

@Serializable
sealed class Image {
    @Serializable
    data class Color(val r: Int, val g: Int, val b: Int, val a: Int = 0): Image()
    @Serializable
    data class ImageResource(val src: String) : Image()
    @Serializable
    data object NoImage : Image()
    
    companion object{
        val default = NoImage
    }
}