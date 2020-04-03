package com.example.eventExplorer.domain.model

sealed class ResponseResult<out T: Any> {
    data class Success<out T : Any>(val data: T) : ResponseResult<T>()
    data class Failure(val message: String) : ResponseResult<Nothing>()
}
