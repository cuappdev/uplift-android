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
    val gymQuery = apolloClient.query(GymListQuery())
    val classQuery = apolloClient.query(ClassListQuery())
    var gymApiFlow =
        gymQuery.toFlow()
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
        private set

    var classesApiFlow =
        classQuery.toFlow()
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
        private set

    /**
     * Retries to load the backend data. Makes another query for all gym and class data.
     */
    fun retry() {
        // TODO: Doesn't work :(
        //  Might need to make classesApiFlow and gymApiFlow into states that can be reacted to...
        //  Will have to refactor places that combine from the flow, though. Kinda annoying.
    }
}
