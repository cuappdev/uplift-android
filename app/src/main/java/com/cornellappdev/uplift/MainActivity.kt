package com.cornellappdev.uplift

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cornellappdev.uplift.ui.MainNavigationWrapper
import com.cornellappdev.uplift.ui.theme.UpliftTheme
import com.cornellappdev.uplift.ui.viewmodels.GymDetailViewModel
import com.cornellappdev.uplift.util.testMorrison

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UpliftTheme {
                MainNavigationWrapper()
            }
        }
    }
}