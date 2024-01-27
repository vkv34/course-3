package core

import kotlin.reflect.KProperty

interface Validator {
//    val errors: MutableMap<String, String>
    val isValid: Boolean
        get() = validate().isEmpty()

    fun validate(): Map<String, String>

    val errorsAsString: String
        get() = validate().map { "${it.key}: ${it.value}" }.joinToString("\n")

}

