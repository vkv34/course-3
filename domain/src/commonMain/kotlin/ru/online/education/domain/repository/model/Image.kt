package ru.online.education.domain.repository.model

import kotlinx.serialization.Serializable

@Serializable
sealed class Image {
    @Serializable
    data class Color(val r: Int, val g: Int, val b: Int, val a: Int = 255): Image()
    @Serializable
    data class ImageResource(val src: String) : Image()
    @Serializable
    data object NoImage : Image()
    
    companion object{
        val default = NoImage
        val defaultColor = Color(230, 142, 10)
    }
}