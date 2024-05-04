package ru.online.education.domain.core

/**
 * A validator that validates that used with objects, which need to be validated
 */
interface Validator {

    val isValid: Boolean
        get() = validate().isEmpty()

    /**
     * Validates the input and returns a map of strings containing the validation results.
     */
    fun validate(): Map<String, String>

    val errorsAsString: String
        get() = validate().map { "${it.key}: ${it.value}" }.joinToString("\n")

}

