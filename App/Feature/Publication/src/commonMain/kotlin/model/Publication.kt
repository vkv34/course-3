package model

data class Publication(
    val id: Int = 0,
    val title: String = "",
    val subTitle: String = "",
    val content: String = "",
    val type: PublicationCategory = PublicationCategory.WithoutAnswer,
    val visible: Boolean = false,
    val temp: Boolean = true,
    val author: String = "",
    val date: String = "",
    val topic: String = ""
)
