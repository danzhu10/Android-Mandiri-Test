package com.mandiri.test.common

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val error: Throwable? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(error: Throwable, message: String?, data: T? = null) :
        Resource<T>(data, message, error)

    class Loading<T> : Resource<T>()

    class Idle<T> : Resource<T>()
}
