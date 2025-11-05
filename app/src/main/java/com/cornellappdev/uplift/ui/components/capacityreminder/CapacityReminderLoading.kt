package com.cornellappdev.uplift.ui.components.capacityreminder

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.montserratFamily
import kotlin.math.cos
import kotlin.math.sin


@Composable

fun CapacityReminderLoading() {
    val infiniteTransition = rememberInfiniteTransition(label = "Capacity Reminder Loading")

    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotationAngle"
    )

    val logoYOffset by infiniteTransition.animateValue(
        initialValue = 0.dp,
        targetValue = (-20).dp, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 750, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logoYOffset",
        typeConverter = Dp.VectorConverter
    )

    val componentSize by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.97f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 750, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "componentSize"
    )

    val textOpacity by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.5f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 750, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "textOpacity"
    )

    val dotCount by infiniteTransition.animateValue(
        initialValue = 1,
        targetValue = 4, // Animate up to (but not including) this value
        typeConverter = Int.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "dotCount"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD9D9D9).copy(alpha = 0.5f))
    ) {
        Box(
            modifier = Modifier
                .height((270 * componentSize).toInt().dp)
                .width((249 * componentSize).toInt().dp)
                .shadow(
                    elevation = 40.dp,
                    shape = RoundedCornerShape(20.dp),
                    ambientColor = Color(0xFFE5ECED),
                    spotColor = Color(0xFFE5ECED)
                )
                .background(PRIMARY_BLACK, RoundedCornerShape(20.dp))
                .align(Alignment.Center)
        ) {
            Box(
                modifier = Modifier
                    .padding(vertical = 25.dp, horizontal = 20.dp)
                    .fillMaxSize()
            ) {
                Text(
                    text = buildString {
                        append("Loading")
                        repeat(dotCount) { append(".") }
                    },
                    fontSize = 24.sp,
                    fontFamily = montserratFamily,
                    fontWeight = FontWeight(500),
                    color = Color.White.copy(alpha = textOpacity),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                )

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Canvas(modifier = Modifier.size(100.dp)) {
                        val center = size / 2f
                        val rayLength = size.minDimension
                        val rayCount = 8
                        val strokeWidth = 8.dp.toPx()

                        for (i in 0 until rayCount) {
                            val angle = i * (360f / rayCount) - 90f + rotationAngle
                            val radians = Math.toRadians(angle.toDouble())
                            val start = Offset(
                                center.width + (rayLength * 0.50f * cos(radians)).toFloat(),
                                center.height + (rayLength * 0.50f * sin(radians)).toFloat()
                            )
                            val end = Offset(
                                center.width + (rayLength * 0.7 * cos(radians)).toFloat(),
                                center.height + (rayLength * 0.7 * sin(radians)).toFloat()
                            )
                            val brush = Brush.radialGradient(
                                colorStops = arrayOf(
                                    0.0f to Color(0xFFFFE894).copy(alpha = 1f - (i * 0.1f)),

                                    1.0f to Color(0xffffffff).copy(alpha = 1f - (i * 0.1f))
                                ),
                                center = Offset(
                                    0.1464f,
                                    0.8418f
                                ),
                                radius = 0.6965f
                            )

                            drawLine(
                                brush = brush,
                                start = start,
                                end = end,
                                strokeWidth = strokeWidth,
                                cap = StrokeCap.Round
                            )
                        }
                    }
                    Image(
                        painter = painterResource(R.drawable.ic_logo_sun_var),
                        contentDescription = "Uplift logo",
                        modifier = Modifier
                            .width(135.dp)
                            .offset(y = 25.dp + logoYOffset),
                        contentScale = ContentScale.FillWidth
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CapacityReminderLoadingPreview() {
    CapacityReminderLoading()
}
