package com.cornellappdev.uplift.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.models.CourtFacility
import com.cornellappdev.uplift.models.TimeInterval
import com.cornellappdev.uplift.ui.components.gymdetail.TimeFlag
import com.cornellappdev.uplift.util.ACCENT_CLOSED
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.PRIMARY_YELLOW
import com.cornellappdev.uplift.util.PRIMARY_YELLOW_BACKGROUND
import com.cornellappdev.uplift.util.getOverallTimeInterval
import com.cornellappdev.uplift.util.montserratFamily

/**
 * Displays gymnasium information for the given gym throughout the week,, defaulting to the day
 * given by parameter [today].
 */
@Composable
fun GymCourtSection(today: Int, court: CourtFacility) {
    var selectedDay by remember {
        mutableIntStateOf(today)
    }

    val gymnasiumInfo = court.hours[selectedDay]

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(5.dp))
        DayOfWeekSelector(today = today) { day ->
            selectedDay = day
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (!gymnasiumInfo.isNullOrEmpty()) {
            for (interval in gymnasiumInfo) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = interval.time.toString(),
                        fontFamily = montserratFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight(300),
                        lineHeight = 26.sp,
                        textAlign = TextAlign.Left,
                        color = PRIMARY_BLACK
                    )
                    Spacer(Modifier.width(8.dp))
                    TimeFlag(interval.type)
                }
                Spacer(Modifier.height(5.dp))
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

// Not in use. Shows a court icon with some text in it. Currently not used since design doesn't
//  account for all possible court data scenarios.
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
