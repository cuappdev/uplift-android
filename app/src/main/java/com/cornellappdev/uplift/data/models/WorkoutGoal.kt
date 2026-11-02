package com.cornellappdev.uplift.data.models

import java.time.Duration
import java.time.Instant

/**
 * How long a goal is locked after it changes. Matches the backend, which rejects goal changes
 * within 30 days of the last one.
 */
val GOAL_LOCK_DURATION: Duration = Duration.ofDays(30)

/**
 * @param goal the user's weekly workout goal in days per week, or null if they haven't set one
 * @param lastGoalChange when the goal last changed, or null if it has never been set. The backend
 * sets this to midnight UTC tomorrow when an existing goal is changed, so it can be in the future.
 */
data class WorkoutGoal(
    val goal: Int?,
    val lastGoalChange: Instant?
) {
    /**
     * Returns the lock on this goal at [now], or null if the goal can be changed.
     */
    fun lockAt(now: Instant): GoalLock? {
        val unlocksAt = lastGoalChange?.plus(GOAL_LOCK_DURATION) ?: return null
        if (!unlocksAt.isAfter(now)) return null
        val remainingNanos = Duration.between(now, unlocksAt).toNanos()
        // Round up so a partial day still counts, e.g. 29.5 days left shows as 30
        val daysRemaining = (remainingNanos + DAY_NANOS - 1) / DAY_NANOS
        return GoalLock(unlocksAt = unlocksAt, daysRemaining = daysRemaining.toInt())
    }

    private companion object {
        val DAY_NANOS = Duration.ofDays(1).toNanos()
    }
}

/**
 * @param unlocksAt when the goal can next be changed
 * @param daysRemaining whole days until [unlocksAt], rounded up
 */
data class GoalLock(
    val unlocksAt: Instant,
    val daysRemaining: Int
)
