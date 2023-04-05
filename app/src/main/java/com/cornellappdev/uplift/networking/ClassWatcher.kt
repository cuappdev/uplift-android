package com.cornellappdev.uplift.networking

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.cornellappdev.uplift.ui.viewmodels.HomeViewModel

/**
 * Watches for classes to be read from backend, updating the UI accordingly.
 */
@Composable
fun ClassWatcher(homeViewModel: HomeViewModel) {
    val classFlowState =
        UpliftApiRepository.classesApiFlow.collectAsState(initial = ApiResponse.Loading)

    when (classFlowState.value) {
        is ApiResponse.Success -> {
            val asUpliftClasses = (classFlowState.value as ApiResponse.Success).data.map {
                it.toUpliftClass()
            }
            homeViewModel.emitClasses(asUpliftClasses)
            UpliftApiRepository.loadUpliftClasses(asUpliftClasses)
        }
        else -> {}
    }
}