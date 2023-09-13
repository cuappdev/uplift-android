package com.cornellappdev.uplift.ui.components.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.util.ACCENT_CLOSED
import com.cornellappdev.uplift.util.ACCENT_OPEN
import com.cornellappdev.uplift.util.ACCENT_ORANGE
import com.cornellappdev.uplift.util.GRAY02
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.colorInterp
import com.cornellappdev.uplift.util.montserratFamily

/**
 * A circular indicator for the capacity at a given gym.
 *
 * @param capacity  A tuple whose first element is the current number of people at the gym
 *                  and whose second element is the max capacity.
 * @param label     The name of the gym placed under this indicator.
 */
@Composable
@Preview
fun GymCapacity(capacity: Pair<Int, Int> = Pair(35, 70), label: String = "Helen Newman") {
    val fraction = capacity.first.toFloat() / capacity.second.toFloat()
    val animatedFraction = remember { Animatable(0f) }

    // When the composable launches, animate the fraction to the capacity fraction.
    LaunchedEffect(animatedFraction) {
        animatedFraction.animateTo(
            fraction,
            animationSpec = tween(durationMillis = 750)
        )
    }

    // Choose a color. If between 0 & 0.5, tween between open and orange. If between 0.5 and 1,
    // tween between orange and closed.
    val color =
        if (fraction > .5)
            colorInterp(
                (fraction - .5f) * 2,
                ACCENT_ORANGE,
                ACCENT_CLOSED
            )
        else
            colorInterp(
                fraction * 2,
                ACCENT_OPEN,
                ACCENT_ORANGE
            )


    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box {
            CircularProgressIndicator(
                color = GRAY02,
                strokeWidth = 8.dp,
                modifier = Modifier.size(72.dp),
                progress = 1f
            )
            CircularProgressIndicator(
                color = color,
                strokeWidth = 8.dp,
                modifier = Modifier.size(72.dp),
                progress = animatedFraction.value,
                strokeCap = StrokeCap.Round
            )
            Text(
                text = "${capacity.first}/${capacity.second}",
                fontFamily = montserratFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight(700),
                color = PRIMARY_BLACK,
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center,
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = label,
            fontFamily = montserratFamily,
            fontSize = 14.sp,
            fontWeight = FontWeight(600),
            color = PRIMARY_BLACK,
            textAlign = TextAlign.Center,
        )
    }
}