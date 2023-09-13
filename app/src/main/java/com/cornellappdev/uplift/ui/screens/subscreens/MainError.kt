package com.cornellappdev.uplift.ui.screens.subscreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.networking.UpliftApiRepository
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.PRIMARY_YELLOW
import com.cornellappdev.uplift.util.montserratFamily

@Composable
fun MainError() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_no_internet),
            contentDescription = "No internet",
            modifier = Modifier.size(86.dp)
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "No connection",
            fontFamily = montserratFamily,
            fontSize = 24.sp,
            fontWeight = FontWeight(700),
            color = PRIMARY_BLACK
        )

        Text(
            text = "Could not connect to Uplift...",
            fontFamily = montserratFamily,
            fontSize = 14.sp,
            fontWeight = FontWeight(400),
            color = PRIMARY_BLACK
        )

        Spacer(Modifier.height(24.dp))

        Button(
            shape = RoundedCornerShape(24.dp),
            onClick = { UpliftApiRepository.retry() },
            colors = ButtonDefaults.buttonColors(backgroundColor = PRIMARY_YELLOW),
            elevation = ButtonDefaults.elevation(0.dp)
        ) {
            Text(
                text = "RETRY",
                fontFamily = montserratFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight(700),
                color = PRIMARY_BLACK
            )
        }
    }
}
