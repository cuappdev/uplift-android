package com.cornellappdev.uplift.ui.components.general

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import com.cornellappdev.uplift.ui.theme.ConfettiColors
import kotlinx.coroutines.delay
import com.cornellappdev.uplift.ui.viewmodels.profile.ConfettiViewModel
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * Shape of a confetti particle used by [ConfettiBurst]
 */
enum class ConfettiShape { CIRCLE, RECTANGLE }

/**
 * UI state backing the Check-In pop-up
 *
 * @param start   Emission point where the particle spawns
 * @param vx      Initial horizontal velocity. Positive is to the right.
 * @param vy0     Initial vertical velocity (px/s). Negative values launch upward.
 * @param size    Base size (px). Used as the radius for circles and scale for rectangles.
 * @param color   Color for the particle.
 * @param shape   Geometric shape to render for this particle.
 * @param rotation Initial rotation (degrees).
 */
private data class ConfettiParticle2D(
    val start: Offset,
    val vx: Float,
    val vy0: Float,
    val size: Float,
    val color: Color,
    val shape: ConfettiShape,
    val rotation: Float
)

@Composable
fun ConfettiBurst(
    confettiViewModel: ConfettiViewModel,
    originRectInRoot: Rect?,
    modifier: Modifier = Modifier,
    particleCount: Int = 30,
    colors: List<Color> = listOf(
        ConfettiColors.Yellow1,
        ConfettiColors.Yellow2,
        ConfettiColors.Yellow3,
        ConfettiColors.Yellow4
    )
) {
    val uiState = confettiViewModel.collectUiStateValue()

    if (!uiState.showing || originRectInRoot == null) {
        return
    }

    val rect = originRectInRoot

    var started by remember(uiState.showing) { mutableStateOf(false) }

    LaunchedEffect(uiState.showing) {
        if (uiState.showing) started = true
    }
    val progress by animateFloatAsState(
        targetValue =  if (started) 1f else 0f,
        animationSpec = tween(durationMillis = 1200, easing = LinearEasing),
        label = "confettiProgress"
    )

    val particles = remember((uiState.showing)) {
        List(particleCount) {
            val x = Random.nextFloat() * rect.width + rect.left
            val y = Random.nextFloat() * rect.height + rect.top
            val start: Offset = Offset(x,y)
            val angle = ((-90f + Random.nextFloat() * 110f) * Math.PI / 180f).toFloat()
            val speed = Random.nextFloat() * 700f + 400f
            val vx = cos(angle) * speed
            val vy0 = sin(angle) * speed
            val size = Random.nextInt(18, 34).toFloat()
            ConfettiParticle2D(
                start = start,
                vx = vx,
                vy0 = vy0,
                size = size,
                color = colors.random(),
                shape = if (Random.nextFloat() < 0.25f) ConfettiShape.CIRCLE else ConfettiShape.RECTANGLE,
                rotation = Random.nextFloat() * 360f
            )
        }
    }


    LaunchedEffect(uiState.showing) {
        if (uiState.showing) {
            delay(1300)
            confettiViewModel.onAnimationFinished()
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val g = 1750f
        val t = progress * 1.2f

        particles.forEach { particle ->
            val x = particle.start.x + particle.vx * t
            val y = particle.start.y + (particle.vy0 * t + 0.5f * g * t * t)

            val alpha = 1f - progress

            val brush = Brush.linearGradient(
                colors = colors,
                start = Offset(x - particle.size * 0.8f, y- particle.size * 0.8f),
                end = Offset(x + particle.size* 0.8f, y+ particle.size * 0.8f)
            )

            when (particle.shape) {
                ConfettiShape.CIRCLE -> {
                    drawCircle(
                        brush = brush,
                        radius = particle.size.toFloat(),
                        center = Offset(x, y),
                        alpha = alpha
                    )
                }

                ConfettiShape.RECTANGLE -> {
                    withTransform({
                        rotate(degrees = particle.rotation, pivot = Offset(x, y))
                    }) {
                        drawRoundRect(
                            brush = brush,
                            topLeft = Offset(
                                x - (particle.size / 8f),
                                y - (particle.size / 2f)
                            ),
                            size = Size(
                                width = particle.size * 0.6f,
                                height = particle.size * 1.8f
                            ),
                            cornerRadius = CornerRadius(2f, 2f),
                            alpha = alpha
                        )
                    }
                }
            }
        }
    }
}