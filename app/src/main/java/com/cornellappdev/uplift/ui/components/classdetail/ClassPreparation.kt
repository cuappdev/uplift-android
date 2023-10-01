package com.cornellappdev.uplift.ui.components.classdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.models.UpliftClass
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.montserratFamily

/**
 * Displays the "PREPARATION" section for an [UpliftClass].
 */
@Composable
fun ClassPreparation(upliftClass: UpliftClass?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(24.dp))
        Text(
            text = "PREPARATION",
            fontWeight = FontWeight(700),
            fontSize = 16.sp,
            lineHeight = 19.5.sp,
            textAlign = TextAlign.Center,
            color = PRIMARY_BLACK,
            fontFamily = montserratFamily
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = upliftClass?.preparation ?: "",
            fontWeight = FontWeight(300),
            fontSize = 14.sp,
            lineHeight = 17.07.sp,
            textAlign = TextAlign.Center,
            color = PRIMARY_BLACK,
            fontFamily = montserratFamily,
            modifier = Modifier.padding(horizontal = 48.dp)
        )
        Spacer(Modifier.height(24.dp))
    }
}
