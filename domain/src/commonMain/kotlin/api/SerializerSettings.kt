package api

import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import model.*

val serializersModule = SerializersModule {
    polymorphic(Any::class) {
        subclass(AuthResponse::class, AuthResponse.serializer())
        subclass(AuthRequest::class, AuthRequest.serializer())
        subclass(CourseDto::class, CourseDto.serializer())
        subclass(UserDto::class, UserDto.serializer())
        subclass(CourseCategoryDto::class, CourseCategoryDto.serializer())
        subclass(PublicationDto::class, PublicationDto.serializer())
        subclass(PublicationOnCourseDto::class, PublicationOnCourseDto.serializer())

        subclass(ListResponse.serializer(CourseDto.serializer()))
    }
    //                polymorphic(Any::class) {
    //                    subclass(List::class, ListSerializer(PolymorphicSerializer(Any::class).nullable))
    //                }

    polymorphic(Image::class) {
        subclass(Image.ImageResource::class, Image.ImageResource.serializer())
        subclass(Image.Color::class, Image.Color.serializer())
    }
}