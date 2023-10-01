package com.cornellappdev.uplift.ui.components.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.montserratFamily

/**
 * Makes a component that displays a sport whose icon is [painter], name is [text].
 * Does [onClick] when this button is clicked.
 */
@Composable
fun SportButton(text: String, painter: Painter, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            shape = CircleShape,
            color = Color.White,
            elevation = 4.dp,
            modifier = Modifier
                .size(64.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = rememberRipple(radius = 32.dp),
                    onClick = onClick
                )
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Icon(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.Center),
                    tint = PRIMARY_BLACK
                )
            }
        }

        Text(
            text = text,
            fontFamily = montserratFamily,
            fontSize = 12.sp,
            fontWeight = FontWeight(500),
            lineHeight = 17.07.sp,
            textAlign = TextAlign.Center,
            color = PRIMARY_BLACK,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}
