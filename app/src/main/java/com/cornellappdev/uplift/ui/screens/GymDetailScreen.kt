package com.cornellappdev.uplift.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.networking.ApiResponse
import com.cornellappdev.uplift.networking.CoilRepository
import com.cornellappdev.uplift.ui.components.GymFacilitySection
import com.cornellappdev.uplift.ui.components.GymHours
import com.cornellappdev.uplift.ui.components.PopularTimesSection
import com.cornellappdev.uplift.ui.components.general.FavoriteButton
import com.cornellappdev.uplift.ui.components.gymdetail.GymAmenitySection
import com.cornellappdev.uplift.ui.components.gymdetail.GymCapacitiesSection
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
                || gym!!.courtInfo.isNotEmpty()
                || gym!!.swimmingInfo != null
                || gym!!.bowlingInfo != null
                || gym!!.miscellaneous.isNotEmpty())

    val bitmapState = CoilRepository.getUrlState(gym?.imageUrl ?: "", LocalContext.current)

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
            Crossfade(
                targetState = bitmapState.value,
                label = "imageFade",
                animationSpec = tween(250)
            ) { apiResponse ->
                when (apiResponse) {
                    is ApiResponse.Success ->
                        Image(
                            bitmap = apiResponse.data,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .graphicsLayer {
                                    alpha = 1 - (scrollState.value.toFloat() / screenHeightPx)
                                },
                            contentScale = ContentScale.Crop,
                        )

                    else ->
                        Image(
                            bitmap = ImageBitmap(height = 1, width = 1),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .graphicsLayer {
                                    alpha = 1 - (scrollState.value.toFloat() / screenHeightPx)
                                }
                                .background(colorInterp(progress, GRAY01, GRAY03)),
                            contentScale = ContentScale.Crop,
                        )
                }
            }
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

            Surface(
                shape = CircleShape,
                color = Color.White,
                modifier = Modifier
                    .size(104.dp)
                    .offset(y = 51.dp)
                    .align(Alignment.BottomCenter)
                    .graphicsLayer {
                        translationY = -0.5f * scrollState.value.toFloat()
                    }
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    if (gym != null && (gym!!.hours[day] == null || !isOpen(gym!!.hours[day]!!))) {
                        Text(
                            text = "CLOSED",
                            fontWeight = FontWeight(700),
                            fontSize = 14.sp,
                            lineHeight = 17.07.sp,
                            textAlign = TextAlign.Center,
                            color = ACCENT_CLOSED,
                            fontFamily = montserratFamily,
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .offset(y = 25.dp)
                        )
                    }else{
                        Text(
                            text = "OPEN",
                            fontWeight = FontWeight(700),
                            fontSize = 14.sp,
                            lineHeight = 17.07.sp,
                            textAlign = TextAlign.Center,
                            color = ACCENT_OPEN,
                            fontFamily = montserratFamily,
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .offset(y = 25.dp)
                        )
                    }

                }
            }

            // CLOSED
//            if (gym != null && (gym!!.hours[day] == null || !isOpen(gym!!.hours[day]!!))) {
//                Box(
//                    modifier = Modifier
//                        .align(Alignment.BottomCenter)
//                        .graphicsLayer {
//                            translationY = -0.5f * scrollState.value
//                        }
//                        .background(PRIMARY_BLACK)
//                        .fillMaxWidth()
//                        .height(60.dp)
//                ) {
//                    Text(
//                        text = "CLOSED",
//                        fontWeight = FontWeight(500),
//                        fontSize = 16.sp,
//                        lineHeight = 19.5.sp,
//                        textAlign = TextAlign.Center,
//                        letterSpacing = 3.sp,
//                        color = Color.White,
//                        fontFamily = montserratFamily,
//                        modifier = Modifier.align(Alignment.Center)
//                    )
//                }
//            }
        }

        if (gym != null) {
            val closed = !isOpen(gym!!.hours[day])
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {

                GymAmenitySection(amenities = gym!!.amenities)


                GymHours(hours = gym!!.hours, day)
                LineSpacer()
                GymCapacitiesSection(
                    capacity = gym!!.upliftCapacity,
                    closed = closed
                )
                if (gym!!.popularTimes.isNotEmpty()) {
                    LineSpacer()
                    PopularTimesSection(gym!!.popularTimes[day])
                }
                LineSpacer()
                if (hasOneFacility)
                    GymFacilitySection(gym!!, day)
                // TODO: Uncomment when classes added to backend
//                GymTodaysClasses(
//                    gymDetailViewModel = gymDetailViewModel,
//                    classDetailViewModel = classDetailViewModel,
//                    navController = navController
//                )
            }
        }

        Spacer(Modifier.height(30.dp))
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
