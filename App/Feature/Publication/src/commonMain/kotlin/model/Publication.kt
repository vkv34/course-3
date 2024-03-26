package model

data class Publication(
    val id: Int,
    val title: String,
    val subTitle: String,
    val content: String,
    val type: PublicationCategory,
    val visible: Boolean,
    val temp: Boolean,
    val author: String,
    val topic: String = ""
)
