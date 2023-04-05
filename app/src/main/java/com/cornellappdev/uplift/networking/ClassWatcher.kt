package com.cornellappdev.uplift.networking

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.cornellappdev.uplift.ui.viewmodels.HomeViewModel

@Composable
fun ClassWatcher(homeViewModel: HomeViewModel) {
    val classFlowState =
        UpliftApiRepository.classesApiFlow.collectAsState(initial = ApiResponse.Loading)

    when (classFlowState.value) {
        is ApiResponse.Success -> {
            homeViewModel.emitClasses((classFlowState.value as ApiResponse.Success).data.map {
                it.toUpliftClass()
            })
        }
        else -> {}
    }
}