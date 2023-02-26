package com.cornellappdev.uplift.ui.components

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
import com.cornellappdev.uplift.util.ACCENT_CLOSED
import com.cornellappdev.uplift.util.montserratFamily

@Composable
fun GymGymnasiumSection(today: Int, gym: Gym) {
    var selectedDay by remember {
        mutableStateOf(today)
    }
    val gymnasiumInfo = gym.gymnasiumInfo[selectedDay]

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(5.dp))
        DayOfWeekSelector(today = today) { day ->
            selectedDay = day
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (gymnasiumInfo != null) {
            for (interval in gymnasiumInfo.hours) {
                Text(
                    text = interval.toString(),
                    fontFamily = montserratFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight(300),
                    lineHeight = 26.sp,
                    textAlign = TextAlign.Left,
                    modifier = Modifier.padding(bottom = 5.dp)
                )
            }
            Spacer(Modifier.padding(bottom = 6.dp))
        } else {
            Text(
                text = "Closed",
                fontFamily = montserratFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight(500),
                lineHeight = 26.sp,
                textAlign = TextAlign.Center,
                color = ACCENT_CLOSED,
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            )
        }
    }
}