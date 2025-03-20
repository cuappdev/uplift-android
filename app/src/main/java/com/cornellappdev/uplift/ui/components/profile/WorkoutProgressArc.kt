package com.cornellappdev.uplift.ui.components.profile

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.GRAY03
import com.cornellappdev.uplift.util.GRAY04
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.PRIMARY_YELLOW
import com.cornellappdev.uplift.util.montserratFamily
import kotlin.math.cos
import kotlin.math.sin

/**
 * A custom circular progress indicator that shows workout progress for the week
 *
 * @param workoutsCompleted Number of workouts completed this week
 * @param workoutGoal Total number of workouts targeted for the week
 */
@Composable
fun WorkoutProgressArc(
    workoutsCompleted: Int,
    workoutGoal: Int,
) {
    // Calculate progress percentage
    val progress = (workoutsCompleted.toFloat() / workoutGoal.toFloat()).coerceIn(0f, 1f)

    // Setup animation
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(progress) {
        animatedProgress.animateTo(
            targetValue = progress,
            animationSpec = tween(
                durationMillis = Math.max(workoutsCompleted * 200, 1000),
                easing = LinearEasing
            )
        )
    }

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.size(250.dp)
    ) {
        // Draw the progress arc
        ProgressArc(animatedProgress, workoutsCompleted, workoutGoal)
        WorkoutFractionTextSection(workoutsCompleted, workoutGoal)
    }
}

@Composable
private fun ProgressArc(
    animatedProgress: Animatable<Float, AnimationVector1D>,
    workoutsCompleted: Int,
    workoutGoal: Int
) {
    val startAngle = 180f;
    val maxSweepAngle = 180f;
    val gradientBrush =
        Brush.horizontalGradient(listOf(Color(0xFFFCF4A1), Color(0xFFFDB041), Color(0xFFFE8F13)))
    Canvas(modifier = Modifier.fillMaxSize()) {
        val strokeWidth = 16.dp.toPx()
        val diameter = size.width - strokeWidth
        val radius = diameter / 2
        val topLeft = Offset(
            (size.width - diameter) / 2,
            (size.height - diameter) / 2
        )
        val arcSize = Size(diameter, diameter)

        // Background arc (light gray)
        drawArc(
            color = GRAY01,
            startAngle = startAngle,
            sweepAngle = maxSweepAngle,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        // Progress arc
        val progressAngle = maxSweepAngle * animatedProgress.value
        drawProgressArc(
            workoutsCompleted,
            workoutGoal,
            gradientBrush,
            startAngle,
            progressAngle,
            topLeft,
            arcSize,
            strokeWidth
        )

        // Draw the progress dot at the end of the progress arc
        val angle = Math.toRadians((startAngle + progressAngle).toDouble())
        val dotRadius = 16.dp.toPx()

        val x = center.x + (radius * cos(angle)).toFloat()
        val y = center.y + (radius * sin(angle)).toFloat()

        // Outer circle
        drawArcSliderOuterCircle(workoutsCompleted, workoutGoal, gradientBrush, dotRadius, x, y)

        // Inner circle
        drawCircle(
            color = Color.White,
            radius = dotRadius * 0.4f,
            center = Offset(x, y)
        )
    }
}

private fun DrawScope.drawProgressArc(
    workoutsCompleted: Int,
    workoutGoal: Int,
    gradientBrush: Brush,
    startAngle: Float,
    progressAngle: Float,
    topLeft: Offset,
    arcSize: Size,
    strokeWidth: Float
) {
    if (workoutsCompleted == workoutGoal) {
        drawArc(
            brush = gradientBrush,
            startAngle = startAngle,
            sweepAngle = progressAngle,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
    } else {
        drawArc(
            color = PRIMARY_YELLOW,
            startAngle = startAngle,
            sweepAngle = progressAngle,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
    }
}


private fun DrawScope.drawArcSliderOuterCircle(
    workoutsCompleted: Int,
    workoutGoal: Int,
    gradientBrush: Brush,
    dotRadius: Float,
    x: Float,
    y: Float
) {
    when (workoutsCompleted) {
        workoutGoal -> {
            drawCircle(
                brush = gradientBrush,
                radius = dotRadius,
                center = Offset(x, y)
            )
        }

        0 -> {
            drawCircle(
                color = GRAY03,
                radius = dotRadius,
                center = Offset(x, y)
            )
        }

        else -> {
            drawCircle(
                color = PRIMARY_YELLOW,
                radius = dotRadius,
                center = Offset(x, y)
            )
        }
    }
}

@Composable
private fun WorkoutFractionTextSection(workoutsCompleted: Int, workoutGoal: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.offset(y = 44.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            WorkoutsCompletedText(workoutsCompleted, workoutGoal)

            Text(
                text = "/ $workoutGoal",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = GRAY03,
                fontFamily = montserratFamily,
                textAlign = TextAlign.Center
            )
        }
        Text(
            text = "Workouts this week",
            fontSize = 14.sp,
            color = GRAY04,
            fontFamily = montserratFamily
        )
    }
}

@Composable
private fun WorkoutsCompletedText(workoutsCompleted: Int, workoutGoal: Int) {
    if (workoutsCompleted == workoutGoal) {
        Text(
            text = "$workoutsCompleted",
            fontSize = 64.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = montserratFamily,
            modifier = Modifier.offset(y = 8.dp),
            style = TextStyle(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFFFCF4A1), Color(0xFFFDB041), Color(0xFFFE8F13))
                )
            )
        )
    } else {
        Text(
            text = "$workoutsCompleted",
            fontSize = 64.sp,
            fontWeight = FontWeight.Bold,
            color = PRIMARY_BLACK,
            fontFamily = montserratFamily,
            modifier = Modifier.offset(y = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WorkoutProgressArcPreview() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        WorkoutProgressArc(
            workoutsCompleted = 5,
            workoutGoal = 5,
        )
    }

}