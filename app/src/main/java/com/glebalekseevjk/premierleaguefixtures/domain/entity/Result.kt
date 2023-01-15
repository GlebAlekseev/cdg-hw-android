package com.glebalekseevjk.premierleaguefixtures.domain.entity

sealed class ResultType<out T> {
    object Loading : ResultType<Nothing>()
    data class Success<T>(val data: T) : ResultType<T>()
    data class Failure<T>(val errorType: ErrorType, val data: T? = null) : ResultType<T>()
}

sealed class ErrorType {
    object Unknown : ErrorType()
}