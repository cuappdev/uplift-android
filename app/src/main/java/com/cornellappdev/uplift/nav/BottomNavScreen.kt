package com.cornellappdev.uplift.nav

import com.cornellappdev.uplift.R

sealed class BottomNavScreen(
    val route: String,
    val painterIds: Pair<Int, Int>,
    val titleText: String
) {
    object Home : BottomNavScreen(
        route = "home",
        painterIds = Pair(R.drawable.ic_dumbbell_inactive, R.drawable.ic_dumbbell_active),
        titleText = "Home"
    )

    object Classes : BottomNavScreen(
        route = "classes",
        painterIds = Pair(R.drawable.ic_classes_inactive, R.drawable.ic_classes_active),
        titleText = "Classes"
    )

    object Sports : BottomNavScreen(
        route = "sports",
        painterIds = Pair(R.drawable.ic_sports_inactive, R.drawable.ic_sports_inactive),
        titleText = "Sports"
    )

    object Favorites : BottomNavScreen(
        route = "favorites",
        painterIds = Pair(R.drawable.ic_favorites_inactive, R.drawable.ic_favorites_inactive),
        titleText = "Favorites"
    )
}