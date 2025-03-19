package com.cornellappdev.uplift.ui.screens.gyms

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
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cornellappdev.uplift.data.repositories.CoilRepository
import com.cornellappdev.uplift.ui.components.gymdetail.GymFacilitySection
import com.cornellappdev.uplift.ui.components.gymdetail.FitnessCenterContent
import com.cornellappdev.uplift.ui.components.gymdetail.GymDetailHero
import com.cornellappdev.uplift.ui.viewmodels.gyms.GymDetailViewModel
import com.cornellappdev.uplift.util.*

/**
 * A screen displaying all the information about a selected gym.
 * @param gymDetailViewModel the view model for the gym detail screen
 * @param onBack the function to call when the back button is pressed
 */
@Composable
fun GymDetailScreen(
    gymDetailViewModel: GymDetailViewModel,
    onBack: () -> Unit
) {
    val gymDetailUiState = gymDetailViewModel.collectUiStateValue()
    val gym = gymDetailUiState.gym ?: return
    val averageCapacitiesList = gymDetailUiState.averageCapacities
    val startTime = gymDetailUiState.startTime
    val selectedPopularTimesIndex = gymDetailUiState.selectedPopularTimesIndex
    val day = todayIndex()

    //tabs
    var tabIndex by remember { mutableIntStateOf(0) }
    val tabs = if (gym.hasOneFacility) {
        listOf("Fitness Center", "Facilities")
    } else {
        listOf("Fitness Center")
    }

    BackHandler {
        onBack()
    }

    val scrollState = rememberScrollState()

    val screenDensity = LocalConfiguration.current.densityDpi / 160f
    val screenHeightPx = LocalConfiguration.current.screenHeightDp.toFloat() * screenDensity


    val bitmapState = CoilRepository.getUrlState(gym.imageUrl ?: "", LocalContext.current)

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
            gym.upliftCapacity,
            gymDetailViewModel::reload
        )

        Divider(
            color = GRAY01,
            thickness = 8.dp,
        )


        GymTabRow(tabIndex, tabs, onTabChange = { tabIndex = it })

        when (tabIndex) {
            // TODO() -> Add Error Handling for null case
            0 -> FitnessCenterContent(
                gym = gym,
                equipmentGroupInfoList = gym.equipmentGroupings,
                averageCapacitiesList = averageCapacitiesList,
                startTime = startTime,
                selectedPopularTimesIndex = selectedPopularTimesIndex
            )

            1 -> GymFacilitySection(
                gym = gym,
                today = day
            )
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

