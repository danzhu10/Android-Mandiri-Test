package com.mandiri.test.common

import retrofit2.Response

suspend fun <T> getResponse(
    request: suspend () -> Response<T>
): Resource<T> {
    return try {
        val result = request.invoke()
        if (result.isSuccessful) {
            Resource.Success(result.body()!!)
        } else {
            Resource.Error(Throwable(result.message()), null, result.body())
        }
    } catch (e: Throwable) {
        Resource.Error(Throwable(e.message), null)
    }
}