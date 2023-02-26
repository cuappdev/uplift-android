package com.cornellappdev.uplift.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.models.Gym
import com.cornellappdev.uplift.util.montserratFamily

@Composable
fun GymTodaysClasses(gym: Gym) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "TODAY'S CLASSES",
            fontFamily = montserratFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight(700),
            lineHeight = 19.5.sp,
            modifier = Modifier.padding(top = 24.dp, bottom = 24.dp),
            textAlign = TextAlign.Center
        )
    }
}