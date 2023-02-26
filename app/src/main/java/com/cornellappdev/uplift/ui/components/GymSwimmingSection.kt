package com.cornellappdev.uplift.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.models.Gym
import com.cornellappdev.uplift.util.LIGHT_YELLOW
import com.cornellappdev.uplift.util.PRIMARY_YELLOW
import com.cornellappdev.uplift.util.montserratFamily

@Composable
fun GymSwimmingSection(today: Int, gym: Gym) {
    var selectedDay by remember { mutableStateOf(today) }
    DayOfWeekSelector(today = today) { day ->
        selectedDay = day
    }
    Row(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.weight(1f))
        Column {
            val swimmingInfo = gym.swimmingInfo[selectedDay]
            if (swimmingInfo != null) {
                for (swimmingTime in swimmingInfo.swimmingTimes) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 7.dp)
                    ) {
                        Text(
                            text = swimmingTime.time.toString(),
                            fontFamily = montserratFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight(300),
                            lineHeight = 26.sp,
                            textAlign = TextAlign.Left,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                        if (swimmingTime.womenOnly) {
                            Box(
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .background(color = LIGHT_YELLOW)
                                    .height(17.dp)
                            ) {
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .background(color = PRIMARY_YELLOW)
                                        .width(1.dp)
                                        .align(
                                            Alignment.CenterStart
                                        )
                                )
                                Text(
                                    text = "women only",
                                    fontFamily = montserratFamily,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight(300),
                                    lineHeight = 14.63.sp,
                                    textAlign = TextAlign.Left,
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .padding(start = 3.5.dp, end = 2.5.dp)
                                )
                            }
                        }
                    }
                }
            } else {
                Text(
                    text = "CLOSED",
                    fontFamily = montserratFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight(300),
                    lineHeight = 26.sp,
                    textAlign = TextAlign.Left,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Spacer(Modifier.height(12.dp))
        }
        Spacer(modifier = Modifier.weight(.5f))
    }
}