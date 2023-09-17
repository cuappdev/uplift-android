package com.cornellappdev.uplift.networking

/**
 * Represents the state of an ApiResponse fetching data of type [T].
 * Can be: [Loading], which represents the call still loading in, [Error], which represents the
 * API call failing, and [Success], which contains a `data` field containing the [T] data.
 */
sealed class ApiResponse<out T : Any> {
    object Loading : ApiResponse<Nothing>()
    object Error : ApiResponse<Nothing>()
    class Success<out T : Any>(val data: T) : ApiResponse<T>()
}
