package model

import kotlinx.datetime.LocalDateTime

data class Publication(
    val id: Int = 0,
    val publicationInCourseId: Int = 0,
    val title: String = "",
    val subTitle: String = "",
    val content: String = "",
    val type: PublicationCategory = PublicationCategory.WithoutAnswer,
    val visible: Boolean = false,
    val temp: Boolean = true,
    val author: String = "",
    val date: String = "",
    val topic: String = "",
    val createdAt: LocalDateTime? = null,
    val deadLine: LocalDateTime? = null
) {
    override fun equals(other: Any?): Boolean =
        if (other !is Publication) {
            false
        } else {
            id == other.id &&
                    publicationInCourseId == other.publicationInCourseId &&
                    title == other.title &&
                    subTitle == other.subTitle &&
                    content == other.content &&
                    type == other.type &&
                    visible == other.visible &&
                    temp == other.temp &&
                    author == other.author &&
                    deadLine == other.deadLine
        }
}
