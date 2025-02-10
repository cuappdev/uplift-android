package com.cornellappdev.uplift.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.cornellappdev.uplift.data.repositories.CoilRepository
import com.cornellappdev.uplift.ui.components.GymFacilitySection
import com.cornellappdev.uplift.ui.components.GymHours
import com.cornellappdev.uplift.ui.components.PopularTimesSection
import com.cornellappdev.uplift.ui.components.gymdetail.GymAmenitySection
import com.cornellappdev.uplift.ui.components.gymdetail.GymCapacitiesSection
import com.cornellappdev.uplift.ui.components.gymdetail.GymDetailHero
import com.cornellappdev.uplift.ui.components.gymdetail.GymTodaysClasses
import com.cornellappdev.uplift.ui.viewmodels.ClassDetailViewModel
import com.cornellappdev.uplift.ui.viewmodels.GymDetailViewModel
import com.cornellappdev.uplift.util.*

/**
 * A screen displaying all the information about a selected gym.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GymDetailScreenCopy(
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
        GymDetailHero(scrollState, bitmapState, screenHeightPx, progress, onBack, gym, day, gym!!.upliftCapacity)

        if (gym != null) {
            val closed = !isOpen(gym!!.hours[day])

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {

                GymAmenitySection(amenities = gym!!.amenities)


                GymHours(hours = gym!!.hours, day, isOpen(gym!!.hours[day]))
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

                GymTodaysClasses(
                    gymDetailViewModel = gymDetailViewModel,
                    classDetailViewModel = classDetailViewModel,
                    navController = navController
                )
            }
        }

        Spacer(Modifier.height(30.dp))
    }
}

///**
// * A [LineSpacer] is a gray line separating sections of a detail screen.
// */
//@Composable
//fun LineSpacer(paddingStart: Dp = 0.dp, paddingEnd: Dp = 0.dp) {
//    Spacer(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(start = paddingStart, end = paddingEnd)
//            .height(1.dp)
//            .background(GRAY01)
//    )
//}

