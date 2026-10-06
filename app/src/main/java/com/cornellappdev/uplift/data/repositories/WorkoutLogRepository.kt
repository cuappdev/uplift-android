package com.cornellappdev.uplift.data.repositories

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Broadcasts a signal whenever a workout is successfully logged, so that other currently-active
 * screens (e.g. history/streaks) can refresh their data without polling or being tightly coupled
 * to whatever triggered the log (check-in, manual entry, etc).
 */
@Singleton
class WorkoutLogRepository @Inject constructor() {
    private val _workoutLoggedEvent = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val workoutLoggedEvent: SharedFlow<Unit> = _workoutLoggedEvent.asSharedFlow()

    fun notifyWorkoutLogged() {
        _workoutLoggedEvent.tryEmit(Unit)
    }
}
