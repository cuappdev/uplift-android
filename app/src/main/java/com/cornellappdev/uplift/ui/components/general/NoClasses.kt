package com.cornellappdev.uplift.ui.components.general

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.montserratFamily

/**
 * Shows a empty state for various components in Uplift having no classes available.
 *
 * @param comingSoon    Should read coming soon text instead of an empty state.
 */
@Composable
fun NoClasses(comingSoon: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.img_green_tea),
            contentDescription = null,
            modifier = Modifier.size(width = 84.39.dp, height = 71.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = if (comingSoon) "Classes coming soon!" else "No classes today",
            fontWeight = FontWeight(700),
            fontSize = 24.sp,
            lineHeight = 29.26.sp,
            textAlign = TextAlign.Center,
            color = PRIMARY_BLACK,
            fontFamily = montserratFamily
        )
        if (!comingSoon)
            Text(
                text = "Relax with some tea or play a sport",
                fontWeight = FontWeight(400),
                fontSize = 17.sp,
                lineHeight = 17.07.sp,
                textAlign = TextAlign.Center,
                color = PRIMARY_BLACK,
                fontFamily = montserratFamily
            )
    }
}
