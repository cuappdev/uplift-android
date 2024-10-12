package com.cornellappdev.uplift.ui.components.capacityreminder

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.util.GRAY00
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.GRAY03
import com.cornellappdev.uplift.util.GRAY04
import com.cornellappdev.uplift.util.PRIMARY_YELLOW
import com.cornellappdev.uplift.util.montserratFamily
import kotlin.math.min

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
    var sliderValue by remember { mutableFloatStateOf(sliderVal) }
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
                Box(
                    modifier = Modifier
                        .width(50.dp)
                        .height(36.dp)
                        .align(Alignment.CenterStart)
                        .offset(
                            x = (sliderValue * LocalDensity.current.density * 112).dp,
                            y = (-40).dp
                        )
                        .background(
                            color = GRAY00,
                            shape = RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${(min(sliderValue, 0.99f) * 100).toInt()}%",
                        fontFamily = montserratFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = GRAY04
                    )
                }
                Slider(
                    modifier = Modifier.fillMaxWidth(),
                    value = sliderValue,
                    onValueChange = {
                        sliderValue = it
                        onSliderValChange(it)
                    },
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
                    text = "99%",
                    fontFamily = montserratFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = GRAY04
                )
            }
        }

    }
}