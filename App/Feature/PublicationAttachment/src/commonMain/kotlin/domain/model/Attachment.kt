package domain.model

sealed class Attachment{
    abstract val id: Int
    abstract val name: String

    abstract val displayName: String
    data class File(
        override val id: Int = 0,
        override val name: String,
        val url: String
    ): Attachment(){
        override val displayName: String
            get() = "Файл"
    }

    data class Link(
        override val id: Int,
        override val name: String,
        val url: String
    ): Attachment(){
        override val displayName: String
            get() = "Ссылка"
    }

    data class Image(
        override val id: Int,
        override val name: String,
        val url: String
    ): Attachment(){
        override val displayName: String
            get() = "Изображение"
    }



}
