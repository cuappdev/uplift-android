package com.cornellappdev.uplift

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.cornellappdev.uplift.ui.MainNavigationWrapper
import com.cornellappdev.uplift.ui.theme.UpliftTheme

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