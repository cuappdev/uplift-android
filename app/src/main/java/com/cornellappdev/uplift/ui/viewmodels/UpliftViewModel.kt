package com.cornellappdev.uplift.ui.viewmodels

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


abstract class UpliftViewModel<UiState>(initialUiState: UiState) : ViewModel() {
    private val _uiStateFlow = MutableStateFlow(initialUiState)
    val uiStateFlow: StateFlow<UiState> = _uiStateFlow.asStateFlow()

    /**
     * Collect the current UI state value.
     */
    @Composable
    fun collectUiStateValue(): UiState = uiStateFlow.collectAsState().value

    /**
     * Apply a mutation to the current UI state.
     * @param mutation The mutation to apply to the current UI state.
     */
    fun applyMutation(mutation: UiState.() -> UiState) {
        _uiStateFlow.value = _uiStateFlow.value.mutation()
    }

    /**
     * Collect a flow and call a collector function on each emission.
     * @param flow The flow to collect.
     * @param collector The function to call on each emission.
     */
    fun <T> asyncCollect(flow: StateFlow<T>, collector: (T) -> Unit): Job {
        return viewModelScope.launch {
            flow.collect { collector(it) }
        }
    }

    protected fun getStateValue(): UiState {
        return _uiStateFlow.value
    }
}