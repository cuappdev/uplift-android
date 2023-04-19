package com.cornellappdev.uplift.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.models.Gym
import com.cornellappdev.uplift.models.TimeInterval
import com.cornellappdev.uplift.util.*

/**
 * Displays gymnasium information for the given gym throughout the week,, defaulting to the day
 * given by parameter [today].
 */
@Composable
fun GymGymnasiumSection(today: Int, gym: Gym) {
    var selectedDay by remember {
        mutableStateOf(today)
    }
    val gymnasiumInfo = gym.gymnasiumInfo?.get(selectedDay)

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
                    modifier = Modifier.padding(bottom = 5.dp),
                    color = PRIMARY_BLACK
                )
            }
            Spacer(Modifier.padding(bottom = 16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                for (i in gymnasiumInfo.courts.indices) {
                    val courtInfo = gymnasiumInfo.courts[i]
                    CourtComponent(
                        times = courtInfo.hours,
                        overallTimeInterval = getOverallTimeInterval(gymnasiumInfo.hours),
                        title = if (gymnasiumInfo.courts.size > 1) "Court #${i + 1}" else "Court",
                        courtName = courtInfo.name.uppercase()
                    )
                }
            }
            if (gymnasiumInfo.courts.isNotEmpty()) {
                Spacer(modifier = Modifier.height(13.dp))
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
        }
    }
}


@Composable
fun CourtComponent(
    times: List<TimeInterval>,
    overallTimeInterval: TimeInterval,
    title: String,
    courtName: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            fontFamily = montserratFamily,
            fontSize = 14.sp,
            fontWeight = FontWeight(600),
            lineHeight = 17.07.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(bottom = 9.dp),
            color = PRIMARY_BLACK
        )
        Box {
            Icon(
                painter = painterResource(id = R.drawable.court_lines),
                contentDescription = null,
                modifier = Modifier
                    .background(PRIMARY_YELLOW_BACKGROUND)
                    .size(width = 124.dp, height = 164.dp),
                tint = PRIMARY_YELLOW
            )
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = courtName,
                    fontFamily = montserratFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight(700),
                    lineHeight = 17.07.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(bottom = 4.dp),
                    color = PRIMARY_BLACK
                )

                // BEFORE ...
                if (getOverallTimeInterval(times).start == overallTimeInterval.start &&
                    getOverallTimeInterval(times).end != overallTimeInterval.end
                ) {
                    Text(
                        text = "BEFORE ${getOverallTimeInterval(times).end}",
                        fontFamily = montserratFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight(500),
                        lineHeight = 14.63.sp,
                        textAlign = TextAlign.Center,
                        color = PRIMARY_BLACK
                    )
                } else if (getOverallTimeInterval(times).start != overallTimeInterval.start &&
                    getOverallTimeInterval(times).end == overallTimeInterval.end
                ) {
                    Text(
                        text = "AFTER ${getOverallTimeInterval(times).start}",
                        fontFamily = montserratFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight(500),
                        lineHeight = 14.63.sp,
                        textAlign = TextAlign.Center,
                        color = PRIMARY_BLACK
                    )
                } else if (getOverallTimeInterval(times).start == overallTimeInterval.start &&
                    getOverallTimeInterval(times).end == overallTimeInterval.end
                ) {
                    Text(
                        text = "ALL DAY",
                        fontFamily = montserratFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight(500),
                        lineHeight = 14.63.sp,
                        textAlign = TextAlign.Center,
                        color = PRIMARY_BLACK
                    )
                } else
                    for (i in times.indices) {
                        Text(
                            text = times[i].toString(),
                            fontFamily = montserratFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight(500),
                            lineHeight = 14.63.sp,
                            textAlign = TextAlign.Center,
                            color = PRIMARY_BLACK
                        )
                        if (i != times.size - 1) {
                            Spacer(modifier = Modifier.height(2.dp))
                        }
                    }
            }
        }
    }
}