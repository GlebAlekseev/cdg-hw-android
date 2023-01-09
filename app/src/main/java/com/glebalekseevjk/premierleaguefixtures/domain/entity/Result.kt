package com.glebalekseevjk.premierleaguefixtures.domain.entity


enum class ResultStatus {
    SUCCESS,
    LOADING,
    FAILURE,
}

data class Result<T>(
    val status: ResultStatus,
    val data: T
)