package com.cornellappdev.uplift.ui.screens

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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.cornellappdev.uplift.nav.navigateToGym
import com.cornellappdev.uplift.ui.components.general.UpliftTopBar
import com.cornellappdev.uplift.ui.components.home.BriefClassInfoCard
import com.cornellappdev.uplift.ui.components.home.HomeCard
import com.cornellappdev.uplift.ui.components.home.SportButton
import com.cornellappdev.uplift.ui.viewmodels.ClassDetailViewModel
import com.cornellappdev.uplift.ui.viewmodels.GymDetailViewModel
import com.cornellappdev.uplift.ui.viewmodels.HomeViewModel
import com.cornellappdev.uplift.util.GRAY04
import com.cornellappdev.uplift.util.montserratFamily

/**
 * The home page of Uplift.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel(),
    navController: NavHostController,
    classDetailViewModel: ClassDetailViewModel = viewModel(),
    gymDetailViewModel: GymDetailViewModel
) {
    val titleText = homeViewModel.titleFlow.collectAsState().value
    val upliftClasses = homeViewModel.classesFlow.collectAsState().value
    val sportsList = homeViewModel.sportsFlow.collectAsState().value
    val gymsList = homeViewModel.gymFlow.collectAsState().value

    val mainScrollState = rememberLazyListState()
    val classRowState = rememberLazyListState()
    val sportsRowState = rememberLazyListState()

    LazyColumn(state = mainScrollState, modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        stickyHeader {
            UpliftTopBar(showIcon = true, title = titleText)
        }

        // TODAY'S CLASSES
        item {
            Spacer(Modifier.height(24.dp))
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

            LazyRow(
                state = classRowState,
                contentPadding = PaddingValues(
                    horizontal = 16.dp
                ),
                modifier = Modifier.padding(top = 12.dp, bottom = 24.dp)
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
                Text(
                    text = "Edit",
                    fontFamily = montserratFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight(700),
                    lineHeight = 17.07.sp,
                    textAlign = TextAlign.Center,
                    color = GRAY04,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .clickable {

                        }
                )
            }

            LazyRow(
                state = sportsRowState,
                contentPadding = PaddingValues(
                    horizontal = 16.dp
                ),
                modifier = Modifier.padding(top = 12.dp, bottom = 24.dp)
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
                Text(
                    text = "Edit",
                    fontFamily = montserratFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight(700),
                    lineHeight = 17.07.sp,
                    textAlign = TextAlign.Center,
                    color = GRAY04,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .clickable {

                        }
                )
            }
        }

        items(items = gymsList) { gym ->
            HomeCard(gym) {
                navController.navigateToGym(gymDetailViewModel = gymDetailViewModel, gym = gym)
            }
        }

        item {
            Spacer(Modifier.height(84.dp))
        }
    }
}