package com.cornellappdev.uplift.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.data.models.gymdetail.UpliftGym
import com.cornellappdev.uplift.ui.components.gymdetail.TimeFlag
import com.cornellappdev.uplift.util.ACCENT_CLOSED
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.montserratFamily

/**
 * Displays swimming information for the given gym throughout the week, defaulting to the day
 * given by parameter [today].
 */
@Composable
fun GymSwimmingSection(today: Int, gym: UpliftGym) {
    var selectedDay by remember { mutableIntStateOf(today) }
    val swimmingInfo = gym.swimmingInfo?.get(selectedDay)

    DayOfWeekSelector(today = today) { day ->
        selectedDay = day
    }
    Spacer(Modifier.height(12.dp))
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
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
                            modifier = Modifier.padding(start = 8.dp),
                            color = PRIMARY_BLACK
                        )
                        if (swimmingTime.womenOnly) {
                            TimeFlag("women only")
                        }
                    }
                }
            } else {
                Text(
                    text = "Closed",
                    fontFamily = montserratFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight(500),
                    lineHeight = 26.sp,
                    textAlign = TextAlign.Center,
                    color = ACCENT_CLOSED,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(Modifier.height(12.dp))
        }
        Spacer(modifier = Modifier.weight(.5f))
    }
}
