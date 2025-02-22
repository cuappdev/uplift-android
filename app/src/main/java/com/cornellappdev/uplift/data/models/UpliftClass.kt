package com.cornellappdev.uplift.data.models

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.cornellappdev.uplift.datastoreRepository
import com.cornellappdev.uplift.util.calendarDayOfWeekToString
import com.cornellappdev.uplift.util.calendarDayToString
import java.util.Calendar

/** An [UpliftClass] object represents all the data needed about a particular fitness class. */
data class UpliftClass(
    val name: String,
    val id: String,
    val gymId: String,
    val location: String,
    val instructorName: String,
    val date: Calendar,
    val time: TimeInterval,
    val functions: List<String>,
    val preparation: String,
    val description: String,
    val imageUrl: String,

    var nextSessions: SnapshotStateList<UpliftClass> = mutableStateListOf(),
    val favoriteState: State<Boolean> = mutableStateOf(false)
) {
    /**
     * Returns a boolean indicating whether this gym is favorited or not. Safe for recomposition.
     */
    @Composable
    fun isFavorite(): Boolean {
        return datastoreRepository.favoriteClassesFlow.collectAsState().value.contains(id)
    }

    /**
     * Toggles the favorite status of this gym.
     */
    fun toggleFavorite() {
        datastoreRepository.saveFavoriteClass(
            id,
            !datastoreRepository.favoriteClassesFlow.value.contains(id)
        )
    }

    /**
     * Returns a full [String] representation of the date of this gym.
     *
     * Example: "Tuesday, Oct 8"
     */
    fun dateAsString(): String {
        return "${calendarDayOfWeekToString(date)}, ${calendarDayToString(date)}"
    }

    /**
     * Returns a formatted string that displays all the functions of this class.
     *
     * Example: "Core  ·  Overall Fitness  ·  Stability"
     */
    fun functionsAsString(): String {
        var result = ""
        functions.forEachIndexed { i, function ->
            if (i != 0) result += "  "
            result += function
            if (i != functions.size - 1) result += "  ·"
        }

        return result
    }
}
