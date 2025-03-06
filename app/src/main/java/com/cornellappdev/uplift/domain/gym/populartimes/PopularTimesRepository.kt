package com.cornellappdev.uplift.domain.gym.populartimes

interface PopularTimesRepository {
    /**
     * @param facilityId The ID of the gym's facility to get the popular times for.
     * @return The popular times for the gym.
     */
    suspend fun getPopularTimes(facilityId: Int): List<PopularTime>
}