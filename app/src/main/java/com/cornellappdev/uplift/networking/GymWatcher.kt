package com.cornellappdev.uplift.networking

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.cornellappdev.uplift.ui.viewmodels.HomeViewModel

/**
 * Watches for gyms to be read from backend, updating the UI accordingly.
 */
@Composable
fun GymWatcher(homeViewModel: HomeViewModel) {
    val gymFlowState =
        UpliftApiRepository.gymApiFlow.collectAsState(initial = ApiResponse.Loading)

    when (gymFlowState.value) {
        is ApiResponse.Success -> {
            val asUpliftGyms = (gymFlowState.value as ApiResponse.Success).data.map {
                it.toUpliftGym()
            }
            homeViewModel.emitGyms(asUpliftGyms)
            UpliftApiRepository.loadUpliftGyms(asUpliftGyms)
        }
        else -> {}
    }
}