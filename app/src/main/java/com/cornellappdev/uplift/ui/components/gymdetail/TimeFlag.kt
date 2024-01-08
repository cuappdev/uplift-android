package com.cornellappdev.uplift.ui.components.gymdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.util.LIGHT_YELLOW
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.PRIMARY_YELLOW
import com.cornellappdev.uplift.util.montserratFamily

/**
 * A Flag placed next to a time interval. Can display text such as "women only".
 */
@Composable
fun TimeFlag(text: String) {
    Box(
        modifier = Modifier
            .padding(start = 8.dp)
            .background(color = LIGHT_YELLOW)
            .height(17.dp)
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .background(color = PRIMARY_YELLOW)
                .width(1.dp)
                .align(
                    Alignment.CenterStart
                )
        )
        Text(
            text = text,
            fontFamily = montserratFamily,
            fontSize = 12.sp,
            fontWeight = FontWeight(300),
            lineHeight = 14.63.sp,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(start = 3.5.dp, end = 2.5.dp),
            color = PRIMARY_BLACK
        )
    }
}
