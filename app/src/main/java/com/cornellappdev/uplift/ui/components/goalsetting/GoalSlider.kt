package com.cornellappdev.uplift.ui.components.goalsetting

import android.R.attr.end
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.GRAY04
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.PRIMARY_YELLOW
import com.cornellappdev.uplift.util.montserratFamily
import kotlin.math.roundToInt

private const val MIN_DAYS = 1
private const val MAX_DAYS = 7
private val ThumbSize = 26.dp
private val LabelEdgeInset = 5.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    isOnboarding: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp, horizontal = 18.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        val daysPerWeekQuestion = stringResource(R.string.goals_days_per_week_question)
        if (!isOnboarding) {
            val setAPlan = stringResource(R.string.goals_set_a_plan)
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
                        append(setAPlan)
                    }
                    withStyle(
                        style = SpanStyle(
                            fontFamily = montserratFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PRIMARY_BLACK
                        )
                    ) {
                        append(daysPerWeekQuestion)
                    }
                },
                modifier = Modifier.padding(end = 30.dp)
            )
        } else {
            Text(
                text = daysPerWeekQuestion,
                fontFamily = montserratFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = PRIMARY_BLACK,
                modifier = Modifier.padding(end = 30.dp)
            )

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .fillMaxHeight()
                    .background(PRIMARY_YELLOW)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.goals_unchangeable_notice),
                fontFamily = montserratFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = PRIMARY_BLACK
            )
        }
        Slider(
            modifier = Modifier
                .fillMaxWidth()
                .layout { measurable, constraints ->
                    val thumbPx = ThumbSize.roundToPx()
                    val placeable = measurable.measure(
                        constraints.copy(
                            minWidth = constraints.maxWidth + thumbPx,
                            maxWidth = constraints.maxWidth + thumbPx
                        )
                    )
                    layout(constraints.maxWidth, placeable.height) {
                        placeable.place(-thumbPx / 2, 0)
                    }
                },
            value = value,
            valueRange = MIN_DAYS.toFloat()..MAX_DAYS.toFloat(),
            steps = MAX_DAYS - MIN_DAYS - 1,
            onValueChange = {
                onValueChange(it)
            },
            thumb = {
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    modifier = Modifier.size(ThumbSize),
                    shadowElevation = 3.dp,
                ) {}
            },
            track = { sliderState ->
                val fraction by remember {
                    derivedStateOf {
                        (sliderState.value - sliderState.valueRange.start) /
                                (sliderState.valueRange.endInclusive - sliderState.valueRange.start)
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
        StepLabels()
    }
}

/**
 * Displays the day labels spread across the full width of the slider track.
 */
@Composable
private fun StepLabels() {
    Layout(
        modifier = Modifier.fillMaxWidth(),
        content = {
            (MIN_DAYS..MAX_DAYS).forEach { day ->
                Text(
                    text = day.toString(),
                    fontFamily = montserratFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = PRIMARY_BLACK
                )
            }
        }
    ) { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints.copy(minWidth = 0)) }
        val width = constraints.maxWidth
        val height = placeables.maxOfOrNull { it.height } ?: 0
        layout(width, height) {
            if (placeables.isEmpty()) return@layout
            // First label starts LabelEdgeInset from the track's left edge and last label ends
            // LabelEdgeInset from its right edge; labels in between are evenly spaced.
            val insetPx = LabelEdgeInset.toPx()
            val firstCenter = insetPx + placeables.first().width / 2f
            val lastCenter = width - insetPx - placeables.last().width / 2f
            val lastIndex = (placeables.size - 1).coerceAtLeast(1)
            placeables.forEachIndexed { index, placeable ->
                val centerX = firstCenter + (lastCenter - firstCenter) * index / lastIndex
                placeable.place((centerX - placeable.width / 2f).roundToInt(), 0)
            }
        }
    }
}
