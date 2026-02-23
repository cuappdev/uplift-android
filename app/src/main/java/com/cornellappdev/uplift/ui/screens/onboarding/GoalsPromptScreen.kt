package com.cornellappdev.uplift.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.ui.components.goalsetting.GoalSlider
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.montserratFamily

/**
 * @param goalValue: value of the goal slider
 * @param onGoalValueChange: callback for when the goal slider value is changed
 * @return GoalPromptScreen composable
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalPromptScreen(
    /* TODO: Replace functions with viewmodel calls */
    goalValue: Float,
    onGoalValueChange: (Float) -> Unit
) {

    Scaffold(
        topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Set your goals.",
                            modifier = Modifier.padding(top = 20.dp),
                            fontSize = 24.sp,
                            fontFamily = montserratFamily,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Left
                        )
                    },
                    modifier = Modifier
                        .height(120.dp)
                        .shadow(elevation = 20.dp, ambientColor = GRAY01),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White,
                    )

                )
        },
        modifier = Modifier.fillMaxSize(),
    ) { padding ->
        Column(
            modifier = Modifier
                .background(color = Color.White)
                .padding(
                    top = padding.calculateTopPadding(),
                )
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            /* Groups the goal slider and workout reminders together */
            Column {
                GoalSlider(value = goalValue, onValueChange = onGoalValueChange)

            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun GoalPromptScreenPreview() {
    var sliderVal by remember { mutableFloatStateOf(1f) }
    GoalPromptScreen(goalValue = sliderVal, onGoalValueChange = { sliderVal = it })
}
