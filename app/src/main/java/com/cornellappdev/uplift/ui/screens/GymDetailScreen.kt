package com.cornellappdev.uplift.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.components.GymHours
import com.cornellappdev.uplift.ui.components.PopularTimesSection
import com.cornellappdev.uplift.ui.viewmodels.GymDetailViewModel
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.montserratFamily
import java.util.*

@Composable
fun GymDetailScreen(
    gymDetailViewModel: GymDetailViewModel
) {
    val gym by gymDetailViewModel.gymFlow.collectAsState()
    val day = (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2) % 7

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {
        // Top Part
        Box(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = gym?.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.FillBounds
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_back_arrow),
                contentDescription = null,
                modifier = Modifier
                    .align(
                        Alignment.TopStart
                    )
                    .padding(top = 47.dp, start = 22.dp),
                tint = Color.White
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_star),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 47.dp, end = 21.dp),
                tint = Color.White
            )
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = gym?.name?.uppercase() ?: "",
                fontWeight = FontWeight(700),
                fontSize = 36.sp,
                lineHeight = 44.sp,
                textAlign = TextAlign.Center,
                letterSpacing = 1.13.sp,
                color = Color.White,
                fontFamily = montserratFamily
            )
        }
        // Hours
        Column(modifier = Modifier.fillMaxWidth()) {
            if (gym != null) {
                GymHours(hours = gym!!.hours, day)
                LineSpacer()
                PopularTimesSection(gym!!.popularTimes)
                LineSpacer()
                Spacer(modifier = Modifier.height(24.dp))
            }
            Text(
                text = "FACILITIES",
                fontFamily = montserratFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight(700),
                lineHeight = 19.5.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/**
 * A [LineSpacer] is a gray line separating sections of a detail screen.
 */
@Composable
fun LineSpacer() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(GRAY01)
    )
}