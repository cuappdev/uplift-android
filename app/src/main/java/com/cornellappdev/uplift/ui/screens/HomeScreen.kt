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
import com.cornellappdev.uplift.networking.ApiResponse
import com.cornellappdev.uplift.ui.screens.subscreens.MainLoaded
import com.cornellappdev.uplift.ui.viewmodels.ClassDetailViewModel
import com.cornellappdev.uplift.ui.viewmodels.GymDetailViewModel
import com.cornellappdev.uplift.ui.viewmodels.HomeViewModel

/**
 * The home page of Uplift.
 */
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel(),
    navController: NavHostController,
    classDetailViewModel: ClassDetailViewModel = viewModel(),
    gymDetailViewModel: GymDetailViewModel
) {
    val titleText = homeViewModel.titleFlow.collectAsState().value
    val classesState = homeViewModel.classesFlow.collectAsState().value
    val sportsList = homeViewModel.sportsFlow.collectAsState().value
    val gymsState = homeViewModel.gymFlow.collectAsState().value

    // Loaded!
    if (gymsState is ApiResponse.Success && classesState is ApiResponse.Success) {
        val gymsList = gymsState.data
        val classesList = classesState.data
        MainLoaded(
            gymDetailViewModel = gymDetailViewModel,
            classDetailViewModel = classDetailViewModel,
            sportsList = sportsList,
            upliftClasses = classesList,
            gymsList = gymsList,
            navController = navController,
            titleText = titleText
        )
    }
    // Some error
    else if (gymsState == ApiResponse.Error || classesState == ApiResponse.Error) {
        // TODO: Error...
    }
    // At least one is still loading.
    else {
        // TODO: Loading...
    }
}