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
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.networking.ApiResponse
import com.cornellappdev.uplift.networking.CoilRepository
import com.cornellappdev.uplift.ui.components.GymFacilitySection
import com.cornellappdev.uplift.ui.components.GymHours
import com.cornellappdev.uplift.ui.components.PopularTimesSection
import com.cornellappdev.uplift.ui.components.general.FavoriteButton
import com.cornellappdev.uplift.ui.components.gymdetail.FitnessCenterContent
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
fun GymDetailScreen(
    gymDetailViewModel: GymDetailViewModel = viewModel(),
    navController: NavHostController,
    onBack: () -> Unit
) {
    val gym by gymDetailViewModel.gymFlow.collectAsState()
    val day = todayIndex()

    //tabs
    var tabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("FITNESS CENTER", "FACILITIES")

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
        GymDetailHero(
            scrollState,
            bitmapState,
            screenHeightPx,
            progress,
            onBack,
            gym,
            day,
            gym!!.upliftCapacity,
            gymDetailViewModel::reload
        )

        Divider(
            color = GRAY01,
            thickness = 8.dp,
        )

        if (gym != null) {
            GymTabRow(tabIndex, tabs, onTabChange = { tabIndex = it })
            when (tabIndex) {
                0 -> FitnessCenterContent(
                    gym = gym
                )
                1 -> GymFacilitySection(
                    gym = gym!!,
                    today = day
                )
            }
        }

        Spacer(Modifier.height(30.dp))
    }
}

@Composable
private fun GymTabRow(tabIndex: Int, tabs: List<String>, onTabChange: (Int) -> Unit = {}) {
    TabRow(
        selectedTabIndex = tabIndex,
        containerColor = Color.White,
        indicator = { tabPositions ->
            SecondaryIndicator(
                Modifier.tabIndicatorOffset(tabPositions[tabIndex]),
                height = 2.dp,
                color = PRIMARY_YELLOW
            )
        },
        divider = {
            Divider(
                color = GRAY01,
                thickness = 1.dp,
            )
        }
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                text = {
                    Text(
                        text = title,
                        color = if (tabIndex == index) PRIMARY_BLACK else GRAY04,
                        fontFamily = montserratFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                selected = tabIndex == index,
                onClick = { onTabChange(index) },
                selectedContentColor = GRAY01,
            )
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

