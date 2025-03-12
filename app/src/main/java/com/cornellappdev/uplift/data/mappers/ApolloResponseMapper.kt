package com.cornellappdev.uplift.data.mappers

import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation

/**
 * Maps an ApolloResponse to a generic Kotlin result. It either provides the data with no errors, or
 * a failure response containing the error message in the throwable.
 */
//fun <T : Operation.Data> ApolloResponse<T>.toResult(): Result<T> {
//
//
//}