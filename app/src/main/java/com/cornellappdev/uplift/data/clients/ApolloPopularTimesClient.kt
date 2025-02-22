package com.cornellappdev.uplift.data.clients

import com.apollographql.apollo3.ApolloClient
import com.cornellappdev.uplift.PopularTimesQuery
import com.cornellappdev.uplift.data.mappers.toPopularTime
import com.cornellappdev.uplift.domain.gym.populartimes.PopularTime
import com.cornellappdev.uplift.domain.gym.populartimes.PopularTimesClient

class ApolloPopularTimesClient(
    private val apolloClient: ApolloClient
) : PopularTimesClient {
    override suspend fun getPopularTimes(facilityId: Int): List<PopularTime> {
        return apolloClient.query(
            PopularTimesQuery(facilityId)
        ).execute().data?.getHourlyAverageCapacitiesByFacilityId?.mapNotNull {
            it?.toPopularTime()
        } ?: emptyList()
    }
}