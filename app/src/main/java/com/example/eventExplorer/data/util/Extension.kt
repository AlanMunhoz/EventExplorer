package com.example.eventExplorer.data.util

import com.example.eventExplorer.domain.model.ResponseResult
import retrofit2.Response

fun <T: Any> Response<T>.responseResult() : ResponseResult<T> {
    return this.body()?.let {
        ResponseResult.Success(it)
    } ?: run {
        ResponseResult.Failure("HttpCode: ${this.code()}")
    }
}

