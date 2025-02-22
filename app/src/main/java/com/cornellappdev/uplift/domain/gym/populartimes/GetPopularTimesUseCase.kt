package com.cornellappdev.uplift.domain.gym.populartimes

class GetPopularTimesUseCase(
    private val popularTimesClient: PopularTimesClient
) {
    /**
     * @param facilityId The ID of the gym's facility to get the popular times for.
     * @return The popular times for the gym.
     */
    suspend fun execute(facilityId: Int): List<PopularTime> {
        return popularTimesClient.getPopularTimes(facilityId)
    }

}