package com.cornellappdev.uplift.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.components.GymFacilitySection
import com.cornellappdev.uplift.ui.components.GymHours
import com.cornellappdev.uplift.ui.components.GymTodaysClasses
import com.cornellappdev.uplift.ui.components.PopularTimesSection
import com.cornellappdev.uplift.ui.viewmodels.GymDetailViewModel
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.isCurrentlyOpen
import com.cornellappdev.uplift.util.montserratFamily
import java.util.*

/**
 * A screen displaying all the information about a selected gym.
 */
@Composable
fun GymDetailScreen(
    gymDetailViewModel: GymDetailViewModel = viewModel()
) {
    val gym by gymDetailViewModel.gymFlow.collectAsState()
    val day = ((Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2) + 7) % 7

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // Top Part
        Box(modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                alpha = 1 - (scrollState.value.toFloat() / 2000)
                translationY = 0.5f * scrollState.value
            }) {
            AsyncImage(
                model = gym?.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
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
            Image(
                painter = painterResource(id = if (gym != null && gym!!.isFavorite()) R.drawable.ic_star_filled else R.drawable.ic_star),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 47.dp, end = 21.dp)
                    .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                        gym?.toggleFavorite()
                    }
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

            // CLOSED
            if (gym != null && (gym!!.hours[day] == null || !isCurrentlyOpen(gym!!.hours[day]!!))) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .background(PRIMARY_BLACK)
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    Text(
                        text = "CLOSED",
                        fontWeight = FontWeight(500),
                        fontSize = 16.sp,
                        lineHeight = 19.5.sp,
                        textAlign = TextAlign.Center,
                        letterSpacing = 3.sp,
                        color = Color.White,
                        fontFamily = montserratFamily,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
        Column(modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)) {
            if (gym != null) {
                GymHours(hours = gym!!.hours, day)
                LineSpacer()
                PopularTimesSection(gym!!.popularTimes)
                LineSpacer()
                GymFacilitySection(gym!!, day)
                GymTodaysClasses(gym!!)
            }
        }
    }
}

/**
 * A [LineSpacer] is a gray line separating sections of a detail screen.
 */
@Composable
fun LineSpacer(paddingStart: Dp = 0.dp, paddingEnd: Dp = 0.dp) {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = paddingStart, end = paddingEnd)
            .height(1.dp)
            .background(GRAY01)
    )
}