package com.cornellappdev.uplift.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.cornellappdev.uplift.ui.components.general.UpliftTopBar
import com.cornellappdev.uplift.ui.components.home.BriefClassInfoCard
import com.cornellappdev.uplift.ui.components.home.SportButton
import com.cornellappdev.uplift.ui.viewmodels.ClassDetailViewModel
import com.cornellappdev.uplift.ui.viewmodels.HomeViewModel
import com.cornellappdev.uplift.util.GRAY04
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.util.montserratFamily
import com.cornellappdev.uplift.util.sports

/**
 * The home page of Uplift.
 */
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel(),
    navController: NavHostController,
    classDetailViewModel: ClassDetailViewModel = viewModel()
) {
    val titleText = homeViewModel.titleFlow.collectAsState().value
    val upliftClasses = homeViewModel.classesFlow.collectAsState().value

    val mainScrollState = rememberLazyListState()
    val classRowState = rememberLazyListState()

    UpliftTopBar(showIcon = true, title = titleText)

    LazyColumn(state = mainScrollState, modifier = Modifier.fillMaxSize()) {
        // TODAY'S CLASSES
        item {
            Spacer(Modifier.height(120.dp))
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
                    text = "Favorite Sports",
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
                    modifier = Modifier.clickable {

                    }
                )
            }

            LazyRow(
                state = classRowState,
                contentPadding = PaddingValues(
                    horizontal = 16.dp
                ),
                modifier = Modifier.padding(top = 12.dp, bottom = 24.dp)
            ) {
                items(items = sports) { sport ->
                    SportButton(text = sport.name, painterResource(id = sport.painterId)) {

                    }
                    Spacer(Modifier.width(24.dp))
                }
            }
        }
    }
}