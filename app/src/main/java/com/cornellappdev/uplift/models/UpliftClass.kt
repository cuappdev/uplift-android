package com.cornellappdev.uplift.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.cornellappdev.uplift.util.calendarDayOfWeekToString
import com.cornellappdev.uplift.util.calendarDayToString
import java.util.*

/** An [UpliftClass] object represents all the data needed about a particular fitness class. */
data class UpliftClass(
    val name: String,
    val location: String,
    val instructorName: String,
    val minutes: Int,
    val date: Calendar,
    val time: TimeInterval,
    val functions: List<String>,
    val preparation: String,
    val description: String,
    val imageUrl : String,
    val nextSessions: List<UpliftClass>,

    val favoriteState: State<Boolean> = mutableStateOf(false)
) {
    /**
     * Returns a boolean indicating whether this gym is favorited or not. Safe for recomposition.
     */
    fun isFavorite(): Boolean {
        return favoriteState.value
    }

    /**
     * Toggles the favorite status of this gym.
     */
    fun toggleFavorite() {
        (favoriteState as MutableState<Boolean>).value = !favoriteState.value
    }

    /**
     * Returns a full [String] representation of the date of this gym.
     *
     * Example: "Tuesday, Oct 8"
     */
    fun dateAsString() : String {
        return "${calendarDayOfWeekToString(date)}, ${calendarDayToString(date)}"
    }

    /**
     * Returns a formatted string that displays all the functions of this class.
     *
     * Example: "Core  ·  Overall Fitness  ·  Stability"
     */
    fun functionsAsString() : String {
        var result = ""
        functions.forEachIndexed { i, function ->
            if (i != 0) result += "  "
            result += function
            if (i != functions.size - 1) result += "  ·"
        }

        return result
    }
}