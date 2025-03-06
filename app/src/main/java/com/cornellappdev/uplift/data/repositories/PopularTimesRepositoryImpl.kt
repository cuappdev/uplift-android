package com.cornellappdev.uplift.data.repositories

import com.apollographql.apollo3.ApolloClient
import com.cornellappdev.uplift.PopularTimesQuery
import com.cornellappdev.uplift.data.mappers.toPopularTime
import com.cornellappdev.uplift.domain.gym.populartimes.PopularTime
import com.cornellappdev.uplift.domain.gym.populartimes.PopularTimesRepository

class PopularTimesRepositoryImpl(
    private val apolloClient: ApolloClient
) : PopularTimesRepository {
    override suspend fun getPopularTimes(facilityId: Int): List<PopularTime> {
        return apolloClient.query(
            PopularTimesQuery(facilityId)
        ).execute().data?.getHourlyAverageCapacitiesByFacilityId?.mapNotNull {
            it?.toPopularTime()
        } ?: emptyList()
    }
}