package com.cornellappdev.uplift.ui.components.goalsetting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.PRIMARY_YELLOW
import com.cornellappdev.uplift.util.montserratFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalSlider(
    value: Float,
    onValueChange: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp) // Increased to make space for labels
            .padding(vertical = 24.dp, horizontal = 18.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontFamily = montserratFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = PRIMARY_BLACK
                    )
                ) {
                    append("Let's set a plan! ")
                }
                withStyle(
                    style = SpanStyle(
                        fontFamily = montserratFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PRIMARY_BLACK
                    )
                ) {
                    append("How many days a week would you like to work out?")
                }
            },
            modifier = Modifier.padding(end = 30.dp)

        )

        Slider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 10.dp),
            value = value,
            valueRange = 0f..7f,
            steps = 6,
            onValueChange = {
                onValueChange(it)
            },
            thumb = {
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    modifier = Modifier.size(26.dp),
                    shadowElevation = 3.dp,
                ) {}
            },
            track = { sliderState ->
                val fraction by remember {
                    derivedStateOf {
                        (sliderState.value - sliderState.valueRange.start) / (sliderState.valueRange.endInclusive - sliderState.valueRange.start)
                    }
                }
                Box(Modifier.fillMaxWidth()) {
                    Box(
                        Modifier
                            .fillMaxWidth(fraction)
                            .align(Alignment.CenterStart)
                            .height(7.dp)
                            .background(PRIMARY_YELLOW, CircleShape)
                    )
                    Box(
                        Modifier
                            .fillMaxWidth(1f - fraction)
                            .align(Alignment.CenterEnd)
                            .height(7.dp)
                            .background(GRAY01, CircleShape)
                    )
                }
            }
        )

        // Adding labels
        Box(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            val stepLabels = listOf("0", "1", "2", "3", "4", "5", "6", "7") // Labels for each step
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    36.dp,
                    Alignment.CenterHorizontally
                )
            ) {
                stepLabels.forEach { label ->
                    Text(
                        text = label,
                        fontFamily = montserratFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = PRIMARY_BLACK
                    )
                }
            }
        }
    }
}


