package com.cornellappdev.uplift.ui.components

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.models.PopularTimes
import com.cornellappdev.uplift.util.*

/**
 * Displays the popular times designated by [popularTimes]. Each column can be tapped on to display
 * a time and description of busyness.
 */
@Composable
fun PopularTimesSection(popularTimes: PopularTimes) {
    var startAnimation by remember { mutableStateOf(false) }
    val animatedHeightScale by animateFloatAsState(
        if (startAnimation) 1.0f else 0f,
        animationSpec = tween(durationMillis = 1000)
    )

    LaunchedEffect(true) {
        startAnimation = true
    }

    val barHeight = 90f
    var selectedPopularTime by remember { mutableStateOf(-1) }
    var lastSelectedPopularTime by remember { mutableStateOf(-1) }

    val animatedOpacity by animateFloatAsState(
        targetValue = if (selectedPopularTime < 0) 0f else 1f
    )

    val animatedLeftRatioState = animateFloatAsState(
        targetValue = if (selectedPopularTime < 0) .5f else
            (selectedPopularTime.toFloat() / (popularTimes.busyList.size - 1))
                .coerceIn(.001f, .999f),
        tween(100, easing = CubicBezierEasing(0f, 0.0f, 0.2f, 1.0f))
    )
    var lastLeftRatio by remember { mutableStateOf(.5f) }
    val animatedLeftRatio =
        if (selectedPopularTime >= 0) animatedLeftRatioState.value else lastLeftRatio

    fun deselect() {
        selectedPopularTime = -1
        lastLeftRatio = animatedLeftRatio
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                deselect()
            }
            .background(Color.White)
    ) {
        // Title
        Text(
            text = "POPULAR TIMES",
            fontFamily = montserratFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight(700),
            lineHeight = 19.5.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            textAlign = TextAlign.Center,
            color = PRIMARY_BLACK
        )


        // 0 AM Usually not too busy
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .padding(bottom = 8.dp, top = 20.dp)
                .alpha(animatedOpacity)
                .scale(scaleX = 1f, scaleY = animatedOpacity),
            verticalAlignment = Alignment.Bottom
        ) {
            val time = popularTimes.startTime.getTimeLater(
                deltaMinutes = 0,
                deltaHours = lastSelectedPopularTime
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
                text = waitTimeFlavorText(
                    popularTimes.busyList[lastSelectedPopularTime.coerceAtLeast(
                        0
                    )]
                ),
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
            .padding(horizontal = 18.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .height(100.dp)

        ) {
            for ((i, popularTime) in popularTimes.busyList.withIndex()) {
                val thisBarHeight = (popularTime * animatedHeightScale * (barHeight / 100f))
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(horizontal = 1.dp)
                        .padding(bottom = 1.dp)
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null
                        ) {
                            if (selectedPopularTime == i) deselect()
                            else {
                                selectedPopularTime = i
                                lastSelectedPopularTime = i
                            }
                        }
                ) {
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
                    if (selectedPopularTime == i)
                        Spacer(
                            modifier = Modifier
                                .width(1.dp)
                                .background(GRAY03)
                                .height((100 - thisBarHeight).dp)
                                .offset(y = (-0).dp)
                                .align(Alignment.TopCenter)
                                .alpha(animatedOpacity)
                        )
                    // The little knob at the bottom as part of the number line.
                    if (popularTime != popularTimes.busyList.last())
                        Spacer(
                            modifier = Modifier
                                .width(1.dp)
                                .height(4.dp)
                                .offset(2.dp, 4.dp)
                                .background(GRAY01)
                                .align(Alignment.BottomEnd)
                        )
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

