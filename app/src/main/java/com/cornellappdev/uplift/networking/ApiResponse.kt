package com.cornellappdev.uplift.networking

sealed class ApiResponse<out T : Any> {
    object Loading : ApiResponse<Nothing>()
    object Error : ApiResponse<Nothing>()
    class Success<out T : Any>(val data: T) : ApiResponse<T>()
}