package com.cornellappdev.uplift.ui.components.profile

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

/**
 * A custom circular progress indicator that shows workout progress for the week
 *
 * @param workoutsCompleted Number of workouts completed this week
 * @param workoutGoal Total number of workouts targeted for the week
 * @param primaryColor The color used for the progress arc (default is yellow)
 * @param backgroundColor The color used for the background arc (default is light gray)
 * @param animate Whether to animate the progress when first displayed
 * @param animationDuration Duration of animation in milliseconds
 * @param modifier Modifier to be applied to the component
 */
@Composable
fun WorkoutProgressArc(
    workoutsCompleted: Int,
    workoutGoal: Int,
    primaryColor: Color = Color(0xFFFFE01B), // Yellow
    backgroundColor: Color = Color(0xFFE0E0E0), // Light Gray
    animate: Boolean = true,
    animationDuration: Int = 1000,
    modifier: Modifier = Modifier
) {
    // Calculate progress percentage
    val progress = (workoutsCompleted.toFloat() / workoutGoal.toFloat()).coerceIn(0f, 1f)

    // Setup animation
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(progress) {
        if (animate) {
            animatedProgress.animateTo(
                targetValue = progress,
                animationSpec = tween(
                    durationMillis = animationDuration,
                    easing = LinearEasing
                )
            )
        } else {
            animatedProgress.snapTo(progress)
        }
    }

    // Constants for the arc
    val startAngle = 135f // Start angle in degrees
    val maxSweepAngle = 270f // Maximum sweep angle in degrees

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(240.dp)
    ) {
        // Draw the progress arc
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 20.dp.toPx()
            val diameter = size.minDimension - strokeWidth
            val radius = diameter / 2
            val topLeft = Offset(
                (size.width - diameter) / 2,
                (size.height - diameter) / 2
            )
            val arcSize = Size(diameter, diameter)

            // Background arc (light gray)
            drawArc(
                color = backgroundColor,
                startAngle = startAngle,
                sweepAngle = maxSweepAngle,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Progress arc (yellow)
            val progressAngle = maxSweepAngle * animatedProgress.value
            drawArc(
                color = primaryColor,
                startAngle = startAngle,
                sweepAngle = progressAngle,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Draw the progress dot at the end of the progress arc
            if (workoutsCompleted > 0) {
                val angle = Math.toRadians((startAngle + progressAngle).toDouble())
                val dotRadius = 10.dp.toPx()

                val x = center.x + (radius * cos(angle)).toFloat()
                val y = center.y + (radius * sin(angle)).toFloat()

                // Outer circle (yellow)
                drawCircle(
                    color = primaryColor,
                    radius = dotRadius,
                    center = Offset(x, y)
                )

                // Inner circle (white)
                drawCircle(
                    color = Color.White,
                    radius = dotRadius * 0.6f,
                    center = Offset(x, y)
                )
            }
        }

        // Progress text in the center
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$workoutsCompleted",
                fontSize = 72.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF333333) // Dark Gray
            )

            Text(
                text = "/ $workoutGoal",
                fontSize = 36.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF999999) // Medium Gray
            )

            Text(
                text = "Workouts this week",
                fontSize = 16.sp,
                color = Color(0xFF333333), // Dark Gray
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutProgressArcPreview() {
    WorkoutProgressArc(
        workoutsCompleted = 4,
        workoutGoal = 5,
        modifier = Modifier.padding(16.dp)
    )
}