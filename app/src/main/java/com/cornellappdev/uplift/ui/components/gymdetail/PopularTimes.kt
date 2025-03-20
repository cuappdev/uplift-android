package com.cornellappdev.uplift.ui.components.gymdetail

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.data.models.TimeOfDay
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.GRAY03
import com.cornellappdev.uplift.util.GRAY04
import com.cornellappdev.uplift.util.LIGHT_YELLOW
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.PRIMARY_YELLOW
import com.cornellappdev.uplift.util.montserratFamily
import com.cornellappdev.uplift.util.waitTimeFlavorText

/**
 * Displays the popular times designated by [busyList] and starting at [startTime]. Each column can be tapped on to display
 * a time and description of busyness.
 */
@Composable
fun PopularTimesSection(
    busyList: List<Int>,
    startTime: TimeOfDay,
    selectedPopularTimesIndex: Int
) {
    var startAnimation by remember { mutableStateOf(false) }
    val animatedHeightScale by animateFloatAsState(
        if (startAnimation) 1.0f else 0f, animationSpec = tween(durationMillis = 1000)
    )

    val barHeight = 90f
    var selectedPopularTime by remember { mutableIntStateOf(selectedPopularTimesIndex) }
    var lastSelectedPopularTime by remember { mutableIntStateOf(selectedPopularTimesIndex) }

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    val animatedOpacity by animateFloatAsState(
        targetValue = if (selectedPopularTime < 0) 0f else 1f
    )

    val animatedLeftRatioState = animateFloatAsState(
        targetValue = if (selectedPopularTime < 0) .5f else (selectedPopularTime.toFloat() / (busyList.size - 1)).coerceIn(
            .001f, .999f
        ), tween(100, easing = CubicBezierEasing(0f, 0.0f, 0.2f, 1.0f))
    )
    var lastLeftRatio by remember { mutableFloatStateOf(.5f) }
    val animatedLeftRatio =
        if (selectedPopularTime >= 0) animatedLeftRatioState.value else lastLeftRatio

    fun deselect() {
        selectedPopularTime = -1
        lastLeftRatio = animatedLeftRatio
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() }, indication = null
            ) {
                deselect()
            }
            .background(Color.White)) {
        // Title
        Text(
            text = "Popular Times",
            fontFamily = montserratFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight(700),
            lineHeight = 20.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            color = PRIMARY_BLACK
        )

        // 0 AM Usually not too busy
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .padding(bottom = 8.dp, top = 20.dp)
                .alpha(animatedOpacity)
                .scale(scaleX = 1f, scaleY = animatedOpacity), verticalAlignment = Alignment.Bottom
        ) {
            val time = startTime.getTimeLater(
                deltaMinutes = 0, deltaHours = lastSelectedPopularTime
            )

            Spacer(modifier = Modifier.weight(animatedLeftRatio))
            Text(
                text = "${if (time.hour == 0) 12 else time.hour} ${if (time.isAM) "AM" else "PM"} ",
                fontFamily = montserratFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight(700),
                lineHeight = 14.63.sp,
                textAlign = TextAlign.Center,
                color = GRAY04
            )
            Text(
                text = waitTimeFlavorText(busyList.getOrNull(lastSelectedPopularTime) ?: 0),
                fontFamily = montserratFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight(500),
                lineHeight = 14.63.sp,
                textAlign = TextAlign.Center,
                color = GRAY04
            )
            Spacer(modifier = Modifier.weight(1 - animatedLeftRatio))

        }
    }

    // Bars
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.Bottom, modifier = Modifier.height(80.dp)

        ) {
            for ((i, popularTime) in busyList.withIndex()) {
                val thisBarHeight = (popularTime * animatedHeightScale * (barHeight / 100f))
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(horizontal = 1.dp)
                        .padding(bottom = 1.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            if (selectedPopularTime == i) deselect()
                            else {
                                selectedPopularTime = i
                                lastSelectedPopularTime = i
                            }
                        }) {
                    // The yellow bar.
                    Surface(
                        modifier = Modifier
                            .height(thisBarHeight.dp)
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter),
                        shape = RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp),
                        color = if (selectedPopularTime == i) PRIMARY_YELLOW else LIGHT_YELLOW
                    ) {}
                    // The gray line above a bar when it's selected.
                    if (selectedPopularTime == i) Spacer(
                        modifier = Modifier
                            .width(1.dp)
                            .background(GRAY03)
                            .height((100 - thisBarHeight).dp)
                            .offset(y = (-0).dp)
                            .align(Alignment.TopCenter)
                            .alpha(animatedOpacity)
                    )
                    // The little knob at the bottom as part of the number line.
                    if (popularTime != busyList.last()) Spacer(
                        modifier = Modifier
                            .width(1.dp)
                            .height(4.dp)
                            .offset(2.dp, 4.dp)
                            .background(GRAY01)
                            .align(Alignment.BottomEnd)
                    )
                    if (i % 3 == 0) {
                        val time =
                            startTime.getTimeLater(deltaMinutes = 0, deltaHours = i)
                        Text(
                            text = "${if (time.hour == 0) 12 else time.hour}${if (time.isAM) "a" else "p"}",
                            fontFamily = montserratFamily,
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight(600),
                            color = PRIMARY_BLACK,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .offset(y = 24.dp)
                        )
                    }
                }
            }
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(GRAY01)
        )
        Spacer(modifier = Modifier.height(26.dp))
    }
}

/**
 * Preview for [PopularTimesSection].
 * Make sure to click "Start Interactive Mode" when previewing to see bars
 */
@Preview(showBackground = true)
@Composable
fun PopularTimesSectionPreview() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PopularTimesSection(
            busyList = listOf(
                20, 30, 40, 50, 50, 45, 35, 40, 50, 70, 80, 90, 95, 85, 70, 65, 20
            ), startTime = TimeOfDay(
                hour = 6, minute = 0, isAM = true,
            ), selectedPopularTimesIndex = -1
        )
    }

}
