package com.cornellappdev.uplift.ui.screens.subscreens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.models.UpliftCapacity
import com.cornellappdev.uplift.models.UpliftClass
import com.cornellappdev.uplift.models.UpliftGym
import com.cornellappdev.uplift.nav.navigateToGym
import com.cornellappdev.uplift.ui.components.general.NoClasses
import com.cornellappdev.uplift.ui.components.general.UpliftTopBar
import com.cornellappdev.uplift.ui.components.home.BriefClassInfoCard
import com.cornellappdev.uplift.ui.components.home.GymCapacity
import com.cornellappdev.uplift.ui.components.home.HomeCard
import com.cornellappdev.uplift.ui.viewmodels.ClassDetailViewModel
import com.cornellappdev.uplift.ui.viewmodels.GymDetailViewModel
import com.cornellappdev.uplift.util.ACCENT_ORANGE
import com.cornellappdev.uplift.util.GRAY00
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.GRAY02
import com.cornellappdev.uplift.util.GRAY04
import com.cornellappdev.uplift.util.asTimeOfDay
import com.cornellappdev.uplift.util.colorInterp
import com.cornellappdev.uplift.util.montserratFamily
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.GregorianCalendar
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainLoaded(
    gymDetailViewModel: GymDetailViewModel,
    classDetailViewModel: ClassDetailViewModel,
    upliftClasses: List<UpliftClass>,
    gymsList: List<UpliftGym>,
    navController: NavHostController,
    titleText: String
) {
    val gymsFavorited = gymsList.filter { gym -> gym.isFavorite() }
    val gymsUnfavorited = gymsList.filter { gym -> !gym.isFavorite() }

    val lastUpdatedCapacity =
        gymsList.map { gym -> gym.upliftCapacity }.fold<UpliftCapacity?, Calendar>(
            GregorianCalendar(1776, 6, 4)
        ) { prev, newCapacity ->
            // ^ I'm making the (bold) assumption here that all CFC updates occurred after
            // Independence Day, July 4th, 1776. (I wouldn't put it against them, though...)

            // Get the latest update.
            newCapacity?.updated.let { newUpdated ->
                if (newUpdated != null && newUpdated > prev) {
                    newUpdated
                } else {
                    prev
                }
            }
        }.asTimeOfDay()

    val gyms = gymsFavorited.toMutableList()
    gyms.addAll(gymsUnfavorited)

    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    var showCapacities by remember { mutableStateOf(false) }

    val capacityAnimation =
        animateFloatAsState(targetValue = if (showCapacities) 1f else 0f, label = "capacities")

    LazyColumn(
        state = lazyListState, modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        stickyHeader {
            UpliftTopBar(showIcon = false, title = titleText) {
                Button(
                    onClick = {
                        showCapacities = !showCapacities
                        coroutineScope.launch {
                            lazyListState.animateScrollToItem(0)
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .width(60.dp)
                        .height(40.dp),
                    elevation = ButtonDefaults.elevation(defaultElevation = 0.dp),
                    contentPadding = PaddingValues(8.dp),
                    border = BorderStroke(1.dp, GRAY01),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorInterp(
                            capacityAnimation.value, Color.White, GRAY00
                        )
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box {
                            CircularProgressIndicator(
                                color = GRAY02,
                                strokeWidth = 3.dp,
                                modifier = Modifier.size(24.dp),
                                progress = 1f
                            )
                            CircularProgressIndicator(
                                color = ACCENT_ORANGE,
                                strokeWidth = 3.dp,
                                modifier = Modifier.size(24.dp),
                                progress = .75f,
                                strokeCap = StrokeCap.Round
                            )
                        }
                        Icon(
                            painter = painterResource(id = R.drawable.ic_chevron_down),
                            contentDescription = null,
                            modifier = Modifier.rotate(capacityAnimation.value * 180f)
                        )
                    }
                }
            }
        }

        // Spacer from header.
        item {
            Spacer(Modifier.height(24.dp))
        }

        // Gym Capacities
        item {
            Column(
                modifier = Modifier
                    .alpha(capacityAnimation.value)
                    .fillMaxWidth()
                    .animateContentSize()
            ) {
                if (showCapacities) {
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
                            text = "Last Updated $lastUpdatedCapacity",
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
                        gyms.filter { gym -> gym.upliftCapacity != null }
                            .let { gymsWithCapacities ->
                                // Place two capacities per row, or one if it's the last row and only 1 is left.
                                val bound = (gymsWithCapacities.size / 2f).roundToInt()
                                for (i in 0 until bound) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 30.dp),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        // First index [i * 2] should always exist.
                                        Box(
                                            modifier = Modifier
                                                .padding(16.dp)
                                                .widthIn(min = 143.dp)
                                                .clickable {
                                                    navController.navigateToGym(
                                                        gymDetailViewModel = gymDetailViewModel,
                                                        gym = gymsWithCapacities[i * 2]
                                                    )
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            GymCapacity(
                                                capacity = gymsWithCapacities[i * 2].upliftCapacity!!,
                                                label = gymsWithCapacities[i * 2].name
                                            )
                                        }

                                        // Second index [i * 2 + 1] may not exist.
                                        if (i * 2 + 1 < gymsWithCapacities.size) {
                                            Box(
                                                modifier = Modifier
                                                    .padding(16.dp)
                                                    .widthIn(min = 143.dp)
                                                    .clickable {
                                                        navController.navigateToGym(
                                                            gymDetailViewModel = gymDetailViewModel,
                                                            gym = gymsWithCapacities[i * 2 + 1]
                                                        )
                                                    },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                GymCapacity(
                                                    capacity = gymsWithCapacities[i * 2 + 1].upliftCapacity!!,
                                                    label = gymsWithCapacities[i * 2 + 1].name
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
                    text = "GYMS",
                    fontFamily = montserratFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight(700),
                    lineHeight = 17.07.sp,
                    textAlign = TextAlign.Center,
                    color = GRAY04
                )
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
            Spacer(Modifier.height(24.dp))
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
                    // TODO: Change to false when backend includes classes.
                    NoClasses(comingSoon = true)
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

        item {
            Spacer(Modifier.height(32.dp))
        }
    }
}
