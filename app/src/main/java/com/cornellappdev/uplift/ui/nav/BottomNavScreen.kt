package com.cornellappdev.uplift.ui.nav

import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.UpliftRootRoute
import kotlinx.serialization.Serializable

@Serializable
sealed class BottomNavScreens<T>(
    val route: T,
    val painterIds: Pair<Int, Int>,
    val titleText: String
) {
    @Serializable
    data object Home : BottomNavScreens<UpliftRootRoute.Home>(
        route = UpliftRootRoute.Home,
        painterIds = Pair(R.drawable.ic_dumbbell_inactive, R.drawable.ic_dumbbell_active),
        titleText = "Home"
    )
    @Serializable
    data object Classes : BottomNavScreens<UpliftRootRoute.Classes>(
        route = UpliftRootRoute.Classes,
        painterIds = Pair(R.drawable.ic_classes_inactive, R.drawable.ic_classes_active),
        titleText = "Classes"
    )
    @Serializable
    data object Profile : BottomNavScreens<UpliftRootRoute.Profile>(
        route = UpliftRootRoute.Profile,
        painterIds = Pair(R.drawable.ic_profile_inactive, R.drawable.ic_profile_active),
        titleText = "Profile"
    )
    @Serializable
    data object Sports : BottomNavScreens<UpliftRootRoute.Sports>(
        route = UpliftRootRoute.Sports,
        painterIds = Pair(R.drawable.ic_sports_inactive, R.drawable.ic_sports_inactive),
        titleText = "Sports"
    )
    @Serializable
    data object Favorites : BottomNavScreens<UpliftRootRoute.Favorites>(
        route = UpliftRootRoute.Favorites,
        painterIds = Pair(R.drawable.ic_favorites_inactive, R.drawable.ic_favorites_inactive),
        titleText = "Favorites"
    )
}