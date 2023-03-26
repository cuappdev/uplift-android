package com.cornellappdev.uplift

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.cornellappdev.uplift.ui.MainNavigationWrapper
import com.cornellappdev.uplift.ui.components.home.HomeCard
import com.cornellappdev.uplift.ui.theme.UpliftTheme
import com.cornellappdev.uplift.ui.viewmodels.GymDetailViewModel
import com.cornellappdev.uplift.util.testMorrison

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gymDetailViewModel = GymDetailViewModel()

        gymDetailViewModel.selectGym(testMorrison)
        setContent {
            UpliftTheme {
                MainNavigationWrapper(gymDetailViewModel = gymDetailViewModel)
            }
        }
    }
}