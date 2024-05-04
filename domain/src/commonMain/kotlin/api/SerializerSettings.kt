package api

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import ru.online.education.domain.repository.model.*

val defaultJson by lazy {
    Json {
        serializersModule = defaultSerializersModule
        isLenient = true
        ignoreUnknownKeys = true
        prettyPrint = true
    }
}

val defaultSerializersModule = SerializersModule {
    polymorphic(BaseModel::class) {
        subclass(AuthResponse::class, AuthResponse.serializer())
        subclass(AuthRequest::class, AuthRequest.serializer())
        subclass(CourseDto::class, CourseDto.serializer())
        subclass(UserDto::class, UserDto.serializer())
        subclass(CourseCategoryDto::class, CourseCategoryDto.serializer())
        subclass(PublicationDto::class, PublicationDto.serializer())
        subclass(PublicationOnCourseDto::class, PublicationOnCourseDto.serializer())
        subclass(PublicationAttachmentDto::class, PublicationAttachmentDto.serializer())
        subclass(UserOnCourseDto::class, UserOnCourseDto.serializer())
        subclass(PublicationAnswerDto::class, PublicationAnswerDto.serializer())
        subclass(PublicationAnswerAttachmentDto::class, PublicationAnswerAttachmentDto.serializer())
//        subclass(ListResponse::class,ListResponse.serializer())
    }
    polymorphic(Any::class) {
        subclass(AuthResponse::class, AuthResponse.serializer())
        subclass(AuthRequest::class, AuthRequest.serializer())
        subclass(CourseDto::class, CourseDto.serializer())
        subclass(UserDto::class, UserDto.serializer())
        subclass(CourseCategoryDto::class, CourseCategoryDto.serializer())
        subclass(PublicationDto::class, PublicationDto.serializer())
        subclass(PublicationOnCourseDto::class, PublicationOnCourseDto.serializer())
        subclass(PublicationAttachmentDto::class, PublicationAttachmentDto.serializer())
        subclass(UserOnCourseDto::class, UserOnCourseDto.serializer())
        subclass(PublicationAnswerDto::class, PublicationAnswerDto.serializer())
        subclass(PublicationAnswerAttachmentDto::class, PublicationAnswerAttachmentDto.serializer())

        subclass(ListResponse.serializer(BaseModel.serializer()))

    }

    polymorphic(Image::class) {
        subclass(Image.ImageResource::class, Image.ImageResource.serializer())
        subclass(Image.Color::class, Image.Color.serializer())
    }
}