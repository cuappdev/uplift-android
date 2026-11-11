package com.cornellappdev.uplift.ui.components.capacityreminder

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.util.GRAY00
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.GRAY03
import com.cornellappdev.uplift.util.GRAY04
import com.cornellappdev.uplift.util.PRIMARY_YELLOW
import com.cornellappdev.uplift.util.montserratFamily
import kotlin.div
import kotlin.math.min
import kotlin.text.toInt
import kotlin.times

/**
 * Section with slider to set the capacity threshold for the capacity reminder feature.
 *
 * @param sliderVal the initial value of the slider
 * @param onSliderValChange the callback when the slider value changes
 */
@SuppressLint("UseOfNonLambdaOffsetOverload")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CapacityThreshold(
    sliderVal: Float = 0.5f,
    onSliderValChange: (Float) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(125.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "CAPACITY THRESHOLD",
            fontFamily = montserratFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = GRAY03
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(94.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                SliderThresholdLabel(sliderVal)
                CapacityThresholdSlider(sliderVal, onSliderValChange)
            }
            CapacityThresholdRangeLabel()
        }

    }
}

@Composable
private fun CapacityThresholdRangeLabel() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "0%",
            fontFamily = montserratFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = GRAY04
        )
        Text(
            text = "100%",
            fontFamily = montserratFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = GRAY04
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CapacityThresholdSlider(sliderVal: Float, onSliderValChange: (Float) -> Unit) {
    Slider(
        modifier = Modifier.fillMaxWidth(),
        value = sliderVal,
        onValueChange = {
            // Round to nearest 0.1 increment (10%)
            val roundedValue = (it * 10).toInt() / 10f
            onSliderValChange(roundedValue)
        },
        steps = 10,
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
        })
}

@Composable
private fun BoxScope.SliderThresholdLabel(sliderVal: Float) {
    val availableWidth = LocalDensity.current.run {
        (LocalConfiguration.current.screenWidthDp - 64).dp.toPx()
    }
    val labelWidth = 50.dp
    val thumbRadius = 13.dp
    val offsetX = (sliderVal * availableWidth -
            LocalDensity.current.run { labelWidth.toPx() / 2 } +
            LocalDensity.current.run { thumbRadius.toPx() })
    Box(
        modifier = Modifier
            .width(labelWidth)
            .height(36.dp)
            .align(Alignment.CenterStart)
            .offset(
                x = with(LocalDensity.current) { offsetX.toDp() },
                y = (-40).dp
            )
            .background(
                color = GRAY00,
                shape = RoundedCornerShape(10.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${(sliderVal * 100).toInt() / 10 * 10}%",
            fontFamily = montserratFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = GRAY04
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CapacityThresholdPreview() {
    val sliderVal = remember { mutableFloatStateOf(0.5f) }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        CapacityThreshold(
            sliderVal = sliderVal.value,
            onSliderValChange = { sliderVal.value = it }
        )
    }
}