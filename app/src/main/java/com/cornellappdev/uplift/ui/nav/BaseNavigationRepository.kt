package com.cornellappdev.uplift.ui.nav

import com.cornellappdev.uplift.util.UIEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Singleton

@Singleton
abstract class BaseNavigationRepository<ScreenType> {

    private val _routeFlow: MutableStateFlow<UIEvent<ScreenType>?> = MutableStateFlow(null)
    val routeFlow: StateFlow<UIEvent<ScreenType>?> = _routeFlow.asStateFlow()

    private val _popBackStackFlow: MutableStateFlow<UIEvent<Unit>?> = MutableStateFlow(null)
    val popBackStackFlow: StateFlow<UIEvent<Unit>?> = _popBackStackFlow.asStateFlow()

    fun navigate(route: ScreenType) {
        _routeFlow.value = UIEvent(route)
    }

    fun popBackStack() {
        _popBackStackFlow.value = UIEvent(Unit)
    }
}