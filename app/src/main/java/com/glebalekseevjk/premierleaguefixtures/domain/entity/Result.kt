package com.glebalekseevjk.premierleaguefixtures.domain.entity

sealed class ResultType {
    object Loading : ResultType()
    class Success<T>(val data: T) : ResultType()
    class Failure(val errorType: ErrorType) : ResultType()
}

sealed class ErrorType {
    object Unknown : ErrorType()
}