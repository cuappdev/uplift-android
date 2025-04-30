package com.cornellappdev.uplift.ui.components.profile.achievements

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.util.BLACK01
import com.cornellappdev.uplift.util.BLACK02
import com.cornellappdev.uplift.util.BLACK03
import com.cornellappdev.uplift.util.GRAY03
import com.cornellappdev.uplift.util.LIGHT_YELLOW
import com.cornellappdev.uplift.util.ORANGE01
import com.cornellappdev.uplift.util.ORANGE02
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.PRIMARY_YELLOW
import com.cornellappdev.uplift.util.YELLOW01
import com.cornellappdev.uplift.util.YELLOW02
import com.cornellappdev.uplift.util.YELLOW03
import com.cornellappdev.uplift.util.YELLOW04
import com.cornellappdev.uplift.util.YELLOW05
import com.cornellappdev.uplift.util.montserratFamily
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.lang.Float.min
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

data class PRCardData(
    val title: String,
    val weight: Int,
    val reps: Int,
    val backgroundGradient: Brush,
    val innerBorderGradient: Brush,
    val titleColor: Color,
    val weightAndRepsColor: Color
)

@Composable
fun PRCards(
    prCardDataList: List<PRCardData>,
    onMoveToBack: (PRCardData) -> Unit
) {
    Box(
        Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        prCardDataList.forEachIndexed { idx, prCardData ->
            key(prCardData) {
                PRCard(
                    order = idx,
                    totalCount = prCardDataList.size,
                    title = prCardData.title,
                    weight = prCardData.weight,
                    reps = prCardData.reps,
                    backgroundGradient = prCardData.backgroundGradient,
                    innerBorderGradient = prCardData.innerBorderGradient,
                    titleColor = prCardData.titleColor,
                    weightAndRepsColor = prCardData.weightAndRepsColor,
                    onMoveToBack = {
                        onMoveToBack(prCardData)
                    },
                    onUpdate = {
                        /* TODO: Fill in with update function */
                    }
                )
            }
        }
    }
}

@Composable
fun PRCard(
    order: Int,
    totalCount: Int,
    title: String,
    weight: Int,
    reps: Int,
    backgroundGradient: Brush,
    innerBorderGradient: Brush,
    titleColor: Color,
    weightAndRepsColor: Color,
    onMoveToBack: () -> Unit,
    onUpdate: () -> Unit
) {
    val animatedScale by animateFloatAsState(
        targetValue = 1f - (totalCount - order) * 0.05f,
    )
    val animatedXOffset by animateDpAsState(
        targetValue = ((totalCount - order) * 49).dp,
    )
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .offset { IntOffset(x = animatedXOffset.roundToPx(), y = 0) }
            .graphicsLayer {
                scaleX = animatedScale
                scaleY = animatedScale
            }
            .swipeToRight { onMoveToBack() }
            .height(311.dp)
            .width(229.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(22.dp),
            )
            .background(backgroundGradient, RoundedCornerShape(22.dp))
            .padding(6.dp)

    ) {
        Box(
            modifier = Modifier
                .width(217.dp)
                .height(298.dp)
                .border(
                    width = 1.4.dp,
                    brush = innerBorderGradient,
                    shape = RoundedCornerShape(16.5.dp)
                )
                .padding(vertical = 39.dp)
        ) {
            PRCardContent(title, titleColor, weight, reps, weightAndRepsColor, onUpdate)
        }
    }
}

@Composable
private fun PRCardContent(
    title: String,
    titleColor: Color,
    weight: Int,
    reps: Int,
    weightAndRepsColor: Color,
    onUpdate: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_pr_card),
            contentDescription = null,
            tint = Color.Unspecified,
        )
        Spacer(modifier = Modifier.height(11.dp))
        Text(
            text = title,
            color = titleColor,
            fontFamily = montserratFamily,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "$weight lb / $reps reps",
            color = weightAndRepsColor,
            fontFamily = montserratFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
        )
        Spacer(modifier = Modifier.weight(1f))
        UpdateButton(
            onUpdate = onUpdate
        )
    }
}

@Composable
private fun UpdateButton(
    onUpdate: () -> Unit
) {
    val yellowBorderGradient = Brush.linearGradient(
        colors = listOf(LIGHT_YELLOW, PRIMARY_YELLOW)
    )
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .width(82.dp)
            .height(32.dp)
            .background(
                color = PRIMARY_BLACK,
                shape = RoundedCornerShape(30.dp)
            )
            .border(
                width = 1.4.dp,
                brush = yellowBorderGradient,
                shape = RoundedCornerShape(30.dp)
            )
            .clickable(
                onClick = onUpdate,
            )
    ) {
        Text(
            text = "Update",
            color = Color.White,
            fontFamily = montserratFamily,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

private fun Modifier.swipeToRight(
    onMoveToBack: () -> Unit
): Modifier = composed {
    val offsetX = remember { Animatable(0f) }
    var clearedHurdle by remember { mutableStateOf(false) }

    pointerInput(Unit) {
        val decay = splineBasedDecay<Float>(this)
        coroutineScope {
            while (true) {
                offsetX.stop()
                val velocityTracker = VelocityTracker()
                awaitPointerEventScope {
                    horizontalDrag(awaitFirstDown().id) { change ->
                        val horizontalDragOffset = offsetX.value + change.positionChange().x
                        launch {
                            offsetX.snapTo(horizontalDragOffset)
                        }
                        velocityTracker.addPosition(change.uptimeMillis, change.position)
                        if (change.positionChange() != Offset.Zero) change.consume()
                    }
                }

                val velocity = velocityTracker.calculateVelocity().x
                val targetOffsetX = decay.calculateTargetValue(offsetX.value, velocity)

                if (targetOffsetX.absoluteValue <= size.width) {
                    launch { offsetX.animateTo(targetValue = 0f, initialVelocity = velocity) }
                } else {
                    val boomerangDuration = 600
                    val maxDistanceToFling =
                        (size.width * 4).toFloat()
                    val distanceToFling = min(
                        targetOffsetX.absoluteValue + size.width, maxDistanceToFling
                    )

                    launch {
                        offsetX.animateTo(
                            targetValue = 0f,
                            initialVelocity = velocity,
                            animationSpec = keyframes {
                                durationMillis = boomerangDuration
                                distanceToFling at (boomerangDuration / 2) with LinearOutSlowInEasing
                                40f at boomerangDuration - 70
                            }
                        ) {
                            if (value >= size.width * 2 && !clearedHurdle) {
                                onMoveToBack()
                                clearedHurdle = true
                            }
                        }
                        clearedHurdle = false // Reset after animation completes
                    }
                }
            }
        }
    }
        .offset { IntOffset(offsetX.value.roundToInt(), 0) }
}

@Preview
@Composable
private fun PRCardsPreview() {
    val blackBackgroundGradient = Brush.linearGradient(
        colors = listOf(BLACK02, BLACK03)
    )
    val yellowBackgroundGradient = Brush.linearGradient(
        colors = listOf(YELLOW01, YELLOW02, YELLOW03)
    )
    val orangeBackgroundGradient = Brush.linearGradient(
        colors = listOf(YELLOW05, ORANGE01, ORANGE02)
    )
    val yellowInnerBorderGradient = Brush.linearGradient(
        colors = listOf(LIGHT_YELLOW, PRIMARY_YELLOW)
    )
    val yellowBlackInnerBorderGradient = Brush.linearGradient(
        colors = listOf(BLACK01, YELLOW04)
    )
    var prCardDataList by remember {
        mutableStateOf(
            listOf(
                PRCardData(
                    title = "Bench Press",
                    weight = 225,
                    reps = 10,
                    backgroundGradient = blackBackgroundGradient,
                    innerBorderGradient = yellowInnerBorderGradient,
                    titleColor = PRIMARY_YELLOW,
                    weightAndRepsColor = GRAY03
                ),
                PRCardData(
                    title = "Squat",
                    weight = 315,
                    reps = 8,
                    backgroundGradient = yellowBackgroundGradient,
                    innerBorderGradient = yellowBlackInnerBorderGradient,
                    titleColor = PRIMARY_BLACK,
                    weightAndRepsColor = PRIMARY_BLACK
                ),
                PRCardData(
                    title = "Deadlift",
                    weight = 405,
                    reps = 5,
                    backgroundGradient = orangeBackgroundGradient,
                    innerBorderGradient = yellowInnerBorderGradient,
                    titleColor = Color.White,
                    weightAndRepsColor = Color.White
                ),
            )
        )
    }
    PRCards(
        prCardDataList = prCardDataList,
        onMoveToBack = { prCardData ->
            prCardDataList = listOf(prCardData) + (prCardDataList - prCardData)
        }
    )
}


