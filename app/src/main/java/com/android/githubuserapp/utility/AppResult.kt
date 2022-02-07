package com.android.githubuserapp.utility

sealed class AppResult<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : AppResult<T>(data)
    class Error<T>(message: String, data: T? = null) : AppResult<T>(data, message)
}