package com.cornellappdev.uplift.data.repositories

import com.apollographql.apollo.ApolloClient
import com.cornellappdev.uplift.PopularTimesQuery
import com.cornellappdev.uplift.data.mappers.toResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PopularTimesRepository @Inject constructor(
    private val apolloClient: ApolloClient
) {
    suspend fun getPopularTimes(facilityId: Int): Result<PopularTimesQuery.Data> {
        return apolloClient.query(
            PopularTimesQuery(facilityId)
        ).execute().toResult()
    }
}