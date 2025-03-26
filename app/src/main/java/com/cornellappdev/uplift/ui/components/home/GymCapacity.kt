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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.data.models.gymdetail.UpliftCapacity
import com.cornellappdev.uplift.util.ACCENT_CLOSED
import com.cornellappdev.uplift.util.ACCENT_OPEN
import com.cornellappdev.uplift.util.ACCENT_ORANGE
import com.cornellappdev.uplift.util.GRAY02
import com.cornellappdev.uplift.util.GRAY04
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.colorInterp
import com.cornellappdev.uplift.util.montserratFamily

/**
 * A circular indicator for the capacity at a given gym.
 *
 * @param capacity  A tuple whose first element is the current number of people at the gym
 *                  and whose second element is the max capacity.
 * @param label     The name of the gym placed under this indicator. Can be null to indicate no
 *                  label.
 * @param closed    If this gym is closed.
 */
@Composable
fun GymCapacity(
    capacity: UpliftCapacity?,
    label: String?,
    closed: Boolean,
    gymDetail: Boolean = false
) {
    val grayedOut = closed || capacity == null

    val fraction = if (!grayedOut) capacity!!.percent.toFloat() else 0f
    val animatedFraction = remember { Animatable(0f) }

    // When the composable launches, animate the fraction to the capacity fraction.
    LaunchedEffect(animatedFraction) {
        animatedFraction.animateTo(
            fraction,
            animationSpec = tween(durationMillis = 750)
        )
    }

    val orangeCutoff = .65f

    // Choose a color. If between 0 & 0.5, tween between open and orange. If between 0.5 and 1,
    // tween between orange and closed.
    val color =
        if (fraction > orangeCutoff)
            colorInterp(
                (fraction - orangeCutoff) / (1 - orangeCutoff),
                ACCENT_ORANGE,
                ACCENT_CLOSED
            )
        else
            colorInterp(
                fraction / orangeCutoff,
                ACCENT_OPEN,
                ACCENT_ORANGE
            )

    val size = if (gymDetail) 104.5.dp else 72.dp
    val percentFontSize =
        (if (gymDetail) 17.sp else 12.sp) * (if (closed || capacity != null) 1f else .9f)
    val labelColor = if (gymDetail) GRAY04 else PRIMARY_BLACK
    val labelFontWeight = if (gymDetail) FontWeight(300) else FontWeight(600)
    val labelPadding = if (gymDetail) 9.dp else 12.dp

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box {
            CircularProgressIndicator(
                color = GRAY02,
                strokeWidth = size / 9,
                modifier = Modifier.size(size),
                progress = 1f
            )
            CircularProgressIndicator(
                color = color,
                strokeWidth = size / 9,
                modifier = Modifier.size(size),
                progress = animatedFraction.value,
                strokeCap = StrokeCap.Round
            )
            Text(
                text = if (closed) "CLOSED"
                else capacity?.percentString() ?: "NO DATA",
                fontFamily = montserratFamily,
                fontSize = percentFontSize,
                fontWeight = FontWeight(700),
                color = if (grayedOut) GRAY02 else PRIMARY_BLACK,
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center,
            )
        }
        if (label != null && !(gymDetail && grayedOut)) {
            Spacer(modifier = Modifier.height(labelPadding))
            Text(
                text = label,
                fontFamily = montserratFamily,
                fontSize = 14.sp,
                fontWeight = labelFontWeight,
                color = labelColor,
                textAlign = TextAlign.Center,
            )
        }
    }
}
