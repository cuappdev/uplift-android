package com.cornellappdev.uplift.ui.screens.gyms.subscreens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.cornellappdev.uplift.data.models.UpliftCapacity
import com.cornellappdev.uplift.data.models.UpliftGym
import com.cornellappdev.uplift.ui.nav.navigateToGym
import com.cornellappdev.uplift.ui.components.general.UpliftTopBar
import com.cornellappdev.uplift.ui.components.home.GymCapacity
import com.cornellappdev.uplift.ui.components.home.HomeCard
import com.cornellappdev.uplift.util.ACCENT_ORANGE
import com.cornellappdev.uplift.util.GRAY00
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.GRAY02
import com.cornellappdev.uplift.util.GRAY04
import com.cornellappdev.uplift.util.GRAY06
import com.cornellappdev.uplift.util.PRIMARY_YELLOW
import com.cornellappdev.uplift.util.asTimeOfDay
import com.cornellappdev.uplift.util.colorInterp
import com.cornellappdev.uplift.util.isOpen
import com.cornellappdev.uplift.util.montserratFamily
import com.cornellappdev.uplift.util.todayIndex
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.GregorianCalendar
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun MainLoaded(
    openGym: (UpliftGym) -> Unit,
    gymsList: List<UpliftGym>,
    navController: NavHostController,
    showCapacities: Boolean,
    titleText: String,
    onToggleCapacities: () -> Unit,
    reload: () -> Unit
) {
    val gymComparator = { gym1: UpliftGym, gym2: UpliftGym ->
        if (isOpen(gym1.hours[todayIndex()]) && !isOpen(gym2.hours[todayIndex()])) {
            -1
        } else if (!isOpen(gym1.hours[todayIndex()]) && isOpen(
                gym2.hours[todayIndex()]
            )
        ) {
            1
        } else {
            if (gym1.getDistance() != null && gym2.getDistance() != null)
                gym1.getDistance()!!.compareTo(gym2.getDistance()!!)
            else
                gym1.name.lowercase().compareTo(gym2.name.lowercase())
        }
    }

    val gymsFavorited = gymsList.filter { gym -> gym.isFavorite() }.sortedWith(gymComparator)
    val gymsUnfavorited = gymsList.filter { gym -> !gym.isFavorite() }.sortedWith(gymComparator)

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

    val capacityAnimation =
        animateFloatAsState(targetValue = if (showCapacities) 1f else 0f, label = "capacities")

    val refresh =
        rememberPullRefreshState(
            refreshing = false,
            onRefresh = reload,
            refreshThreshold = 120.dp
        )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 28.dp)
            .pullRefresh(refresh)
    ) {
        LazyColumn(
            state = lazyListState, modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            stickyHeader {
                UpliftTopBar(showIcon = false, title = titleText) {
                    Button(
                        onClick = {
                            onToggleCapacities()
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
                                modifier = Modifier.rotate(capacityAnimation.value * 180f),
                                tint = GRAY06
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
                            gyms.let { gymsWithCapacities ->
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
                                                .clickable(
                                                    indication = null,
                                                    interactionSource = remember { MutableInteractionSource() }
                                                ) {
                                                    navController.navigateToGym(
                                                        openGym,
                                                        gym = gymsWithCapacities[i * 2]
                                                    )
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            GymCapacity(
                                                capacity = gymsWithCapacities[i * 2].upliftCapacity,
                                                label = gymsWithCapacities[i * 2].name,
                                                closed = !isOpen(gymsWithCapacities[i * 2].hours[todayIndex()])
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
                                                            openGym,
                                                            gym = gymsWithCapacities[i * 2 + 1]
                                                        )
                                                    },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                GymCapacity(
                                                    capacity = gymsWithCapacities[i * 2 + 1].upliftCapacity,
                                                    label = gymsWithCapacities[i * 2 + 1].name,
                                                    closed = !isOpen(gymsWithCapacities[i * 2 + 1].hours[todayIndex()])
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
                        text = "REC CENTERS",
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
                Box(modifier = Modifier.animateItem(fadeInSpec = null, fadeOutSpec = null)) {
                    HomeCard(gym) {
                        navController.navigateToGym(
                            openGym,
                            gym = gym
                        )
                    }
                }
            }

            item {
                Spacer(Modifier.height(32.dp))
            }
        }

        PullRefreshIndicator(
            refreshing = true,
            state = refresh,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-5).dp),
            contentColor = PRIMARY_YELLOW,
            backgroundColor = Color.White
        )
    }
}
