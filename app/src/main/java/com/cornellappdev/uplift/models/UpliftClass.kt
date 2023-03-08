package com.cornellappdev.uplift.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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
}