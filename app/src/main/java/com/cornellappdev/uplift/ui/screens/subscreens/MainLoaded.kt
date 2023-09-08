package com.cornellappdev.uplift.ui.screens.subscreens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.cornellappdev.uplift.models.Sport
import com.cornellappdev.uplift.models.UpliftClass
import com.cornellappdev.uplift.models.UpliftGym
import com.cornellappdev.uplift.nav.navigateToGym
import com.cornellappdev.uplift.ui.components.general.NoClasses
import com.cornellappdev.uplift.ui.components.general.UpliftTopBar
import com.cornellappdev.uplift.ui.components.home.BriefClassInfoCard
import com.cornellappdev.uplift.ui.components.home.GymCapacity
import com.cornellappdev.uplift.ui.components.home.HomeCard
import com.cornellappdev.uplift.ui.components.home.SportButton
import com.cornellappdev.uplift.ui.viewmodels.ClassDetailViewModel
import com.cornellappdev.uplift.ui.viewmodels.GymDetailViewModel
import com.cornellappdev.uplift.util.GRAY04
import com.cornellappdev.uplift.util.montserratFamily
import com.cornellappdev.uplift.util.testMorrison
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainLoaded(
    gymDetailViewModel: GymDetailViewModel,
    classDetailViewModel: ClassDetailViewModel,
    sportsList: List<Sport>,
    upliftClasses: List<UpliftClass>,
    gymsList: List<UpliftGym>,
    navController: NavHostController,
    titleText: String
) {
    val gymsFavorited = gymsList.filter { gym -> gym.isFavorite() }
    val gymsUnfavorited = gymsList.filter { gym -> !gym.isFavorite() }

    val gyms = gymsFavorited.toMutableList()
    gyms.addAll(gymsUnfavorited)

    LazyColumn(
        state = rememberLazyListState(), modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        stickyHeader {
            UpliftTopBar(showIcon = true, title = titleText)
        }

        // Spacer from header.
        item {
            Spacer(Modifier.height(24.dp))
        }

        // Gym Capacities
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "GYM CAPACITIES",
                    fontFamily = montserratFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight(700),
                    lineHeight = 17.07.sp,
                    textAlign = TextAlign.Center,
                    color = GRAY04
                )
                Text(
                    text = "Last Updated 00:00pm",
                    fontFamily = montserratFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight(300),
                    textAlign = TextAlign.Center,
                    color = GRAY04
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // TODO: Since backend is down, using 5 [testMorrison]s. When backend is up,
                //  change to use [gyms] (or [gyms] filtered by which ones have capacity data).
                listOf(
                    testMorrison, testMorrison, testMorrison, testMorrison, testMorrison
                ).let { gymsWithCapacities ->
                    // Place two capacities per row, or one if it's the last row and only 1 is left.
                    val bound = (gymsWithCapacities.size / 2f).roundToInt()
                    for (i in 0 until bound) {
                        Row(horizontalArrangement = Arrangement.Center) {
                            // First index [i * 2] should always exist.
                            Box(modifier = Modifier.padding(16.dp)) {
                                GymCapacity(
                                    capacity = gymsWithCapacities[i * 2].capacity,
                                    label = gymsWithCapacities[i * 2].name
                                )
                            }

                            // Second index [i * 2 + 1] may not exist.
                            if (i * 2 + 1 < gymsWithCapacities.size) {
                                Box(modifier = Modifier.padding(16.dp)) {
                                    GymCapacity(
                                        capacity = gymsWithCapacities[i * 2 + 1].capacity,
                                        label = gymsWithCapacities[i * 2].name
                                    )
                                }
                            }
                        }

                        if (i != bound - 1) {
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }
        }

        // TODAY'S CLASSES
        item {
            Text(
                text = "TODAY'S CLASSES",
                fontFamily = montserratFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight(700),
                lineHeight = 17.07.sp,
                textAlign = TextAlign.Center,
                color = GRAY04,
                modifier = Modifier.padding(start = 16.dp)
            )

            if (upliftClasses.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    NoClasses()
                }
            } else LazyRow(
                state = rememberLazyListState(), contentPadding = PaddingValues(
                    horizontal = 16.dp
                ), modifier = Modifier.padding(top = 12.dp, bottom = 24.dp)
            ) {
                items(items = upliftClasses) { upliftClass ->
                    BriefClassInfoCard(
                        thisClass = upliftClass,
                        navController = navController,
                        classDetailViewModel = classDetailViewModel
                    )
                    Spacer(Modifier.width(16.dp))
                }
            }
        }

        // Favorite Sports
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Your Sports",
                    fontFamily = montserratFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight(700),
                    lineHeight = 17.07.sp,
                    textAlign = TextAlign.Center,
                    color = GRAY04
                )
                Text(text = "Edit",
                    fontFamily = montserratFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight(700),
                    lineHeight = 17.07.sp,
                    textAlign = TextAlign.Center,
                    color = GRAY04,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .clickable {

                        })
            }

            LazyRow(
                state = rememberLazyListState(), contentPadding = PaddingValues(
                    horizontal = 16.dp
                ), modifier = Modifier.padding(top = 12.dp, bottom = 24.dp)
            ) {
                items(items = sportsList) { sport ->
                    SportButton(text = sport.name, painterResource(id = sport.painterId)) {

                    }
                    Spacer(Modifier.width(24.dp))
                }
            }
        }

        // Gyms
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Gyms",
                    fontFamily = montserratFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight(700),
                    lineHeight = 17.07.sp,
                    textAlign = TextAlign.Center,
                    color = GRAY04
                )
                Text(text = "Edit",
                    fontFamily = montserratFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight(700),
                    lineHeight = 17.07.sp,
                    textAlign = TextAlign.Center,
                    color = GRAY04,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .clickable {

                        })
            }
        }

        items(items = gyms, key = { gym -> gym.hashCode() }) { gym ->
            Box(modifier = Modifier.animateItemPlacement()) {
                HomeCard(gym) {
                    navController.navigateToGym(gymDetailViewModel = gymDetailViewModel, gym = gym)
                }
            }
        }

        item {
            Spacer(Modifier.height(84.dp))
        }
    }
}
