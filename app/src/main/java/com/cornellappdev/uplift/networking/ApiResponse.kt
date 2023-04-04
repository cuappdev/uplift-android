package com.cornellappdev.uplift.networking

import com.example.rocketreserver.GymListQuery

sealed class ApiResponse {
    object Loading : ApiResponse()
    object Error : ApiResponse()
    class Success(val launchList: List<GymListQuery.Gym>) : ApiResponse()
}