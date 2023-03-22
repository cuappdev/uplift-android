package com.cornellappdev.uplift.ui.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cornellappdev.uplift.models.TimeOfDay
import com.cornellappdev.uplift.ui.components.general.UpliftTopBar
import com.cornellappdev.uplift.ui.viewmodels.HomeViewModel
import com.cornellappdev.uplift.util.getSystemTime

/**
 * The home page of Uplift.
 */
@Composable
fun HomeScreen(homeViewModel: HomeViewModel = viewModel()) {
    val now = getSystemTime()
    val titleText =
        // 4 AM to 12 PM
        if (now.compareTo(TimeOfDay(4, 0, true)) >= 0 &&
            now.compareTo(TimeOfDay(12, 0, false)) < 0
        )
            "Good Morning!"
        // 12 PM to 6 PM
        else if (now.compareTo(TimeOfDay(12, 0, false)) >= 0 && now.compareTo(
                TimeOfDay(6, 0, false)) < 0)
            "Good Afternoon!"
        // 6 PM to 4 AM
        else "Good Evening!"
    UpliftTopBar(showIcon = true, title = titleText)

}