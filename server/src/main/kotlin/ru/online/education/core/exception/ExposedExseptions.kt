package ru.online.education.core.exception

open class ExposedException(message: String) : Exception(message)

class InsertErrorException(message: String) : ExposedException(message)

class SelectExeption(message: String) : ExposedException(message)
