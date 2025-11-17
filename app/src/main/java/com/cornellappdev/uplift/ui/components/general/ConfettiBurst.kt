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
import com.google.android.play.core.integrity.x
import com.google.android.play.integrity.internal.y
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

/**
 * Renders a confetti burst anchored to a given popup rectangle.(checkinpopup).
 *
 * Reads 'showing' from [ConfettiViewModel]. If false or rect is null, renders nothing.
 * When shown, spawns particles inside the bounds of 'originRectInRoot'.
 * Animates progress from 0 to 1 in 1.2 seconds with linear easing.
 * Applies simple ballistic motion to each particle:
 * x(t) = x0 + vx * t
 * y(t) = y0 +vy0 * t + 0.5 * g * t^2
 * where g = 1750 px/s^2 to pull particles downward.
 * Fades particles out as progress approaches 1.
 * When complete calls [ConfettiViewModel.onAnimationFinished] to hide the confetti.
 */
@Composable
fun ConfettiBurst(
    confettiViewModel: ConfettiViewModel,
    particleSpawningBounds: Rect?,
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

    if (!uiState.showing || particleSpawningBounds == null) {
        return
    }

    val rect = particleSpawningBounds

    var started by remember(uiState.showing) { mutableStateOf(false) }

    LaunchedEffect(uiState.showing) {
        if (uiState.showing) started = true
    }

    // Progress 0 to 1 over 1.2s, used as time 't' in the physics below
    val progress by animateFloatAsState(
        targetValue =  if (started) 1f else 0f,
        animationSpec = tween(durationMillis = 1200, easing = LinearEasing),
        label = "confettiProgress"
    )

    //build particles each with spawn, shape, size and velocity
    val particles = remember((uiState.showing)) {
        List(particleCount) {
            //spawn uniformly inside the rect bounds
            val x = Random.nextFloat() * rect.width + rect.left
            val y = Random.nextFloat() * rect.height + rect.top
            val start: Offset = Offset(x,y)
            //angled straight up with a random right skew to look more natural
            val angle = ((-90f + Random.nextFloat() * 110f) * Math.PI / 180f).toFloat()
            //initial speed
            val speed = Random.nextFloat() * 700f + 400f
            //velocity components
            val vx = cos(angle) * speed
            val vy0 = sin(angle) * speed
            //size in px
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

    //Renders ballistic motion + fade out for each particle
    Canvas(modifier = modifier.fillMaxSize()) {
        //gravity and seconds (time)
        val gravity = 1750f
        val time = progress * 1.2f

        particles.forEach { particle ->
            //position at time
            val posX = particle.start.x + particle.vx * time
            val posY = particle.start.y + (particle.vy0 * time + 0.5f * gravity * time * time)

            // fade progress
            val alpha = 1f - progress

            //gradient brush
            val brush = Brush.linearGradient(
                colors = colors,
                start = Offset(posX - particle.size * 0.8f, posY- particle.size * 0.8f),
                end = Offset(posX + particle.size* 0.8f, posY + particle.size * 0.8f)
            )

            when (particle.shape) {
                ConfettiShape.CIRCLE -> {
                    drawCircle(
                        brush = brush,
                        radius = particle.size.toFloat(),
                        center = Offset(posX, posY),
                        alpha = alpha
                    )
                }

                ConfettiShape.RECTANGLE -> {
                    withTransform({
                        rotate(degrees = particle.rotation, pivot = Offset(posX, posY))
                    }) {
                        drawRoundRect(
                            brush = brush,
                            topLeft = Offset(
                                posX - (particle.size / 8f),
                                posY - (particle.size / 2f)
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