package com.cornellappdev.uplift.ui.components.gymdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.data.models.UpliftCapacity
import com.cornellappdev.uplift.ui.components.home.GymCapacity
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.montserratFamily

/**
 * The Gym Detail's Capacity section.
 */
@Composable
fun GymCapacitiesSection(closed: Boolean, capacity: UpliftCapacity?) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(14.dp))
        // Title
        Text(
            text = "CAPACITY",
            fontFamily = montserratFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight(700),
            lineHeight = 19.5.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = PRIMARY_BLACK
        )
        Row(modifier = Modifier.padding(top = 18.dp, bottom = 24.dp)) {
            GymCapacity(
                capacity = capacity,
                label = capacity?.updatedString(),
                closed = closed,
                gymDetail = true
            )
        }
    }
}
