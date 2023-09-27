package com.cornellappdev.uplift.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.components.GymFacilitySection
import com.cornellappdev.uplift.ui.components.GymHours
import com.cornellappdev.uplift.ui.components.PopularTimesSection
import com.cornellappdev.uplift.ui.components.general.FavoriteButton
import com.cornellappdev.uplift.ui.components.gymdetail.GymCapacitiesSection
import com.cornellappdev.uplift.ui.components.gymdetail.GymTodaysClasses
import com.cornellappdev.uplift.ui.viewmodels.ClassDetailViewModel
import com.cornellappdev.uplift.ui.viewmodels.GymDetailViewModel
import com.cornellappdev.uplift.util.*

/**
 * A screen displaying all the information about a selected gym.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GymDetailScreen(
    gymDetailViewModel: GymDetailViewModel = viewModel(),
    classDetailViewModel: ClassDetailViewModel,
    navController: NavHostController,
    onBack: () -> Unit
) {
    val gym by gymDetailViewModel.gymFlow.collectAsState()
    val day = todayIndex()

    BackHandler {
        onBack()
    }

    val scrollState = rememberScrollState()

    val screenDensity = LocalConfiguration.current.densityDpi / 160f
    val screenHeightPx = LocalConfiguration.current.screenHeightDp.toFloat() * screenDensity

    val hasOneFacility =
        gym != null
                && (gym!!.equipmentGroupings.isNotEmpty()
                || gym!!.gymnasiumInfo != null
                || gym!!.swimmingInfo != null
                || gym!!.bowlingInfo != null
                || gym!!.miscellaneous.isNotEmpty())


    var loading by remember { mutableStateOf(true) }

    val infiniteTransition = rememberInfiniteTransition(label = "gymDetailLoading")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = .5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gymDetailLoading"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color.White)
    ) {
        // Top Part
        Box(modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                translationY = 0.5f * scrollState.value
            })
        {
            AsyncImage(
                model = gym?.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .graphicsLayer {
                        alpha = 1 - (scrollState.value.toFloat() / screenHeightPx)
                    }
                    .then(
                        if (loading) Modifier
                            .background(colorInterp(progress, GRAY01, GRAY03)) else Modifier
                    ),
                contentScale = ContentScale.Crop,
                onState = { state ->
                    loading = state !is AsyncImagePainter.State.Success
                }
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_back_arrow),
                contentDescription = null,
                modifier = Modifier
                    .align(
                        Alignment.TopStart
                    )
                    .padding(top = 47.dp, start = 22.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = onBack
                    ),
                tint = Color.White
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 47.dp, end = 21.dp)
            ) {
                FavoriteButton(
                    filled = (gym != null && gym!!.isFavorite())
                ) { gym?.toggleFavorite() }
            }

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
                        .graphicsLayer {
                            translationY = -0.5f * scrollState.value
                        }
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

        if (gym != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                GymHours(hours = gym!!.hours, day)
                if (gym!!.upliftCapacity != null) {
                    LineSpacer()
                    GymCapacitiesSection(gym!!.upliftCapacity!!)
                }
                if (gym!!.popularTimes.isNotEmpty()) {
                    LineSpacer()
                    PopularTimesSection(gym!!.popularTimes[day])
                }
                LineSpacer()
                if (hasOneFacility)
                    GymFacilitySection(gym!!, day)
                GymTodaysClasses(
                    gymDetailViewModel = gymDetailViewModel,
                    classDetailViewModel = classDetailViewModel,
                    navController = navController
                )
            }
        }

        Spacer(Modifier.height(50.dp))
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
