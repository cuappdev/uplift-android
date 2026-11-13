package com.cornellappdev.uplift.ui.components.general

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.PRIMARY_YELLOW
import com.cornellappdev.uplift.util.montserratFamily


@Composable
fun RadioCheckButton(
    text: String,
    selected: Boolean = false,
    onButtonSelected: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val animatedScale by animateFloatAsState(
            targetValue = if (selected) 1f else 0f,
            animationSpec = tween(durationMillis = 150, easing = FastOutSlowInEasing),
            label = "radioCheckScale"
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onButtonSelected()
            }
        ) {
            // background circle
            Surface(
                modifier = Modifier.size(24.dp),
                shape = CircleShape,
                color = GRAY01
            ) {}

            // yellow circle
            Surface(
                modifier = Modifier
                    .size(24.dp)
                    .scale(animatedScale),
                shape = CircleShape,
                color = PRIMARY_YELLOW
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_checkmark),
                        contentDescription = null,
                        tint = PRIMARY_BLACK
                    )
                }
            }
        }

        Text(
            text = text,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = Color.Black,
            fontFamily = montserratFamily
        )
    }
}

@Preview
@Composable
private fun RadioCheckButtonPreview() {
    var selected by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        RadioCheckButton(
            text = "Custom",
            selected = selected,
            onButtonSelected = { selected = !selected }
        )
    }
}