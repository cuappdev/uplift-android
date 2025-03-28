package com.cornellappdev.uplift.ui.components.general

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.util.GRAY02
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.PRIMARY_YELLOW
import com.cornellappdev.uplift.util.montserratFamily

/**
 * @param onClick the callback that is called when the button is clicked
 * @param enabled whether the button is enabled
 * @param title the text displayed on the button
 * @param width the width of the button
 * @param height the height of the button
 * @param fontSize the font size of the text on the button
 * @return UpliftButton composable that displays a button with the given text
 */
@Composable
fun UpliftButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
    text: String = "SUBMIT",
    width: Dp = 106.dp,
    height: Dp = 42.dp,
    fontSize: Float = 14f,
    containerColor: Color = PRIMARY_YELLOW,
    disabledContainerColor: Color = GRAY02,
    contentColor: Color = PRIMARY_BLACK,
    disabledContentColor: Color = PRIMARY_BLACK,
    elevation: Dp = 4.dp
) {
    val enabledAlpha by animateFloatAsState(
        targetValue = if (enabled) 1f else 0.5f,
        label = "enabled alpha"
    )
    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            disabledContainerColor = disabledContainerColor,
            contentColor = contentColor,
            disabledContentColor = disabledContentColor
        ),
        shape = RoundedCornerShape(38.dp),
        modifier = Modifier
            .width(width)
            .height(height)
            .alpha(enabledAlpha),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = elevation
        ),
        onClick = { onClick() },
        enabled = enabled,
    ) {
        Text(
            text = text,
            color = PRIMARY_BLACK,
            fontFamily = montserratFamily,
            fontSize = fontSize.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.wrapContentSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UpliftButtonPreview() {
    UpliftButton(onClick = { /*TODO*/ })
}