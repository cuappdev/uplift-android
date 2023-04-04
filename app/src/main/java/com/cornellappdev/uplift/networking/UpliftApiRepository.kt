package com.cornellappdev.uplift.networking

import com.apollographql.apollo3.ApolloClient
import com.example.rocketreserver.ClassListQuery
import com.example.rocketreserver.GymListQuery
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

val apolloClient = ApolloClient.Builder()
    .serverUrl("https://uplift-backend.cornellappdev.com")
    .build()

object UpliftApiRepository {
    val gymApiFlow =
        apolloClient.query(GymListQuery()).toFlow()
            .map {
                val gymList = it.data?.gyms?.filterNotNull()
                if (gymList == null) {
                    ApiResponse.Error
                } else {
                    ApiResponse.Success(gymList)
                }
            }
            .catch {
                emit(ApiResponse.Error)
            }

    val classesApiFlow =
        apolloClient.query(ClassListQuery()).toFlow()
            .map {
                val classList = it.data?.classes?.filterNotNull()
                if (classList == null) {
                    ApiResponse.Error
                } else {
                    ApiResponse.Success(classList)
                }
            }
            .catch {
                emit(ApiResponse.Error)
            }
}