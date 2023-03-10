package com.cornellappdev.uplift.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.models.TimeInterval
import com.cornellappdev.uplift.util.montserratFamily

/**
 * An expandable component that displays the hours for the given gym throughout the week.
 * Displays only today's hours by default, but can be tapped to display the hours for the upcoming
 * week.
 */
@Composable
fun GymHours(hours: List<List<TimeInterval>?>, today: Int) {
    var collapsed by remember { mutableStateOf(true) }
    val rotationAnimation by animateFloatAsState(targetValue = if (collapsed) 0f else 90f)
    val collapseAnimation by animateFloatAsState(
        targetValue = if (collapsed) 0f else 7f,
        animationSpec = tween(durationMillis = 300, easing = LinearEasing)
    )

    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "HOURS",
            fontFamily = montserratFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight(700),
            lineHeight = 19.5.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    collapsed = !collapsed
                },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_clock),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.padding(top = 2.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (hours[today] == null) {
                    Text(
                        text = "Closed",
                        fontFamily = montserratFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight(500),
                        lineHeight = 19.5.sp,
                        textAlign = TextAlign.Center
                    )
                } else
                    for (interval in hours[today]!!) {
                        Text(
                            text = interval.toString(),
                            fontFamily = montserratFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight(500),
                            lineHeight = 19.5.sp,
                            textAlign = TextAlign.Center
                        )
                    }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                painter = painterResource(R.drawable.ic_caret_right),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier
                    .padding(top = 7.dp)
                    .rotate(rotationAnimation)
            )
        }
        if (!collapsed)
            for (i in 1 until 7) {
                val animationProgress = (collapseAnimation - i).coerceIn(0f, 1f)
                Column(
                    modifier = Modifier
                        .alpha(animationProgress)
                        .fillMaxWidth()
                        .offset(x = ((1 - animationProgress) * -30f).dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val day = (today + i) % 7
                    Spacer(modifier = Modifier.height(8.dp))
                    HoursOfWeek(
                        when (day) {
                            0 -> "M"
                            1 -> "Tu"
                            2 -> "W"
                            3 -> "Th"
                            4 -> "Fr"
                            5 -> "Sa"
                            else -> "Su"
                        }, hours[day]
                    )
                }
            }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

/**
 * Displays the hours for a particular day of the week.
 *
 * @param day a String representation of the day of the week (e.g. "M", "Tu")
 * @param hours a list of [TimeInterval]s that denote the hours for the day.
 */
@Composable
fun HoursOfWeek(day: String, hours: List<TimeInterval>?) {
    Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = day,
            fontFamily = montserratFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight(500),
            lineHeight = 19.5.sp,
            textAlign = TextAlign.Right
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (hours == null) {
                Text(
                    text = "Closed",
                    fontFamily = montserratFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight(400),
                    lineHeight = 19.5.sp,
                    textAlign = TextAlign.Center
                )
            } else
                for (interval in hours) {
                    Text(
                        text = interval.toString(),
                        fontFamily = montserratFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight(400),
                        lineHeight = 19.5.sp,
                        textAlign = TextAlign.Center
                    )
                }
        }

        Spacer(modifier = Modifier.width(8.dp))
        // Spacer to align the hours correctly even though it doesn't have a chevron.
        Spacer(modifier = Modifier.width(9.dp))
    }
}