package com.cornellappdev.uplift.ui.components.reporting

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.PRIMARY_YELLOW
import com.cornellappdev.uplift.util.montserratFamily

/**
 * SubmitButton is a composable that displays a button for the user to submit their report.
 */
@Composable
fun SubmitButton(
    onSubmit: () -> Unit,
    enabled: Boolean
) {
    val enabledAlpha by animateFloatAsState(
        targetValue = if (enabled) 1f else 0.5f,
        label = "enabled alpha"
    )
    Surface(
        color = PRIMARY_YELLOW,
        shape = RoundedCornerShape(38.dp),
        modifier = Modifier
            .width(106.dp)
            .height(42.dp)
            .alpha(enabledAlpha),
        shadowElevation = 4.dp,
        onClick = { onSubmit() },
        enabled = enabled
    ) {
        Text(
            text = "SUBMIT",
            color = PRIMARY_BLACK,
            fontFamily = montserratFamily,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(12.dp)
        )
    }
}