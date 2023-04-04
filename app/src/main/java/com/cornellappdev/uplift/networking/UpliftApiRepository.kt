package com.cornellappdev.uplift.networking

import com.example.rocketreserver.GymListQuery
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

object UpliftApiRepository {
    val gymApiFlow =
        apolloClient.query(GymListQuery()).toFlow()
            .map {
                val launchList = it.data?.gyms?.filterNotNull()
                if (launchList == null) {
                    ApiResponse.Error
                } else {
                    ApiResponse.Success(launchList)
                }
            }
            .catch {
                emit(ApiResponse.Error)
            }


}