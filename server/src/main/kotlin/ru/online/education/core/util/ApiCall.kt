package ru.online.education.core.util

import core.Validator
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*
import model.BaseModel
import ru.online.education.domain.model.BaseService
import util.ApiResult

inline fun <reified T: BaseModel> dbCall(
    successMessage: String = "",
    errorMessage: String = "",
    call: () -> T
): ApiResult<T> =
    try {
        ApiResult.Success(call(), successMessage)
    } catch (e: Exception) {
        ApiResult.Error(errorMessage, e)
    }

inline fun <reified T: BaseModel> apiCall(
    apiResultCall: () -> ApiResult<T>
): ApiResult<T> =
    try {
        apiResultCall()
    } catch (e: Exception) {
        ApiResult.Error(e.localizedMessage ?: "", e)
    }

suspend inline fun <reified T> PipelineContext<Unit, ApplicationCall>.validateInput(data: T)
        where T : Validator, T : BaseModel {
    if (!data.isValid) {
        call.respond(
            status = HttpStatusCode.BadRequest,
            message = ApiResult.Error<T>(data.errorsAsString) as ApiResult<T>
        )
        finish()
    }
}

suspend inline fun <reified T, reified K> PipelineContext<Unit, ApplicationCall>.createAndRespond(
    service: BaseService<T, K>
)
        where T : BaseModel, T : Validator {
    val input = call.receive<T>()
    validateInput(input)
    val result = service.create(input)
//    when (val result = service.create(input)) {
//        is ApiResult.Success -> {
//            call.respond(
//                status = HttpStatusCode.Created, message = ApiResult.Success(
//                    result.data,
//                    result.message
//                ) as ApiResult<T>
//            )
//            finish()
//        }
//
//        is ApiResult.Error -> {
//            call.respond(status = HttpStatusCode.BadRequest, message = result as ApiResult<T>)
//            finish()
//        }
//
//        is ApiResult.Empty -> TODO()
//        is ApiResult.Loading -> TODO()
//    }
    respond(result)
    /*
        call.respond(status = result.toStatusCode(), message = result)
        finish()
    */
}

suspend inline fun <reified T: BaseModel> PipelineContext<Unit, ApplicationCall>.respond(
    apiResult: ApiResult<T>
) {
    call.respond(status = apiResult.toStatusCode(), apiResult)
    finish()
}
suspend inline fun <reified T: BaseModel> PipelineContext<Unit, ApplicationCall>.respondCreated(
    apiResult: ApiResult<T>
) {
    call.respond(status = apiResult.toStatusCode(true), apiResult)
    finish()
}


suspend inline fun <reified T> PipelineContext<Unit, ApplicationCall>.getAndRespond(getScope: () -> ApiResult<T>) {
    val result = getScope()
    when (result) {
        is ApiResult.Success -> {
            call.respond(status = HttpStatusCode.OK, message = result as ApiResult<T>)
            finish()
        }

        is ApiResult.Error -> {
            call.respond(status = HttpStatusCode.BadRequest, message = result as ApiResult<T>)
            finish()
        }

        is ApiResult.Empty -> TODO()
//        is ApiResult.Loading -> TODO()
    }
}

suspend inline fun <reified T, reified K> PipelineContext<Unit, ApplicationCall>.getAndRespond(
    service: BaseService<T, K>
) where T : BaseModel {
    val page = call.parameters["page"]?.toInt() ?: 0
    val result = service.getAll(page)
//    when (result) {
//        is ApiResult.Success -> {
//            call.respond<ApiResult<ListResponse<T>>>(status = HttpStatusCode.OK, message = result)
//            finish()
//        }
//        is ApiResult.Error -> {
//            call.respond(status = HttpStatusCode.BadRequest, message = result as ApiResult<*>)
//            finish()
//        }
//    }
    call.respond(status = result.toStatusCode(), message = result)

}

inline fun <reified T> ApiResult<T>.toStatusCode(created: Boolean = false) = when (this) {
    is ApiResult.Success -> {
        if (created) HttpStatusCode.Created else HttpStatusCode.OK
    }
    is ApiResult.Error -> HttpStatusCode.BadRequest
    is ApiResult.Empty -> HttpStatusCode.NoContent
//    is ApiResult.Loading -> TODO()
}



