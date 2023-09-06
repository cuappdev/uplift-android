package com.cornellappdev.uplift.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.models.UpliftClass
import com.cornellappdev.uplift.networking.ApiResponse
import com.cornellappdev.uplift.ui.components.ClassInfoCard
import com.cornellappdev.uplift.ui.components.general.CalendarBar
import com.cornellappdev.uplift.ui.components.general.UpliftTopBar
import com.cornellappdev.uplift.ui.viewmodels.ClassDetailViewModel
import com.cornellappdev.uplift.ui.viewmodels.ClassesViewModel
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.montserratFamily
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

/**
 * The main screen for the "Classes" section of the app.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClassScreen(
    classDetailViewModel: ClassDetailViewModel = viewModel(),
    classesViewModel: ClassesViewModel,
    navController: NavHostController
) {
    val classesResponse = classesViewModel.classesFlow.collectAsState()
    val classesList = when (classesResponse.value) {
        ApiResponse.Loading -> listOf()
        ApiResponse.Error -> listOf()
        is ApiResponse.Success ->
            (classesResponse.value as ApiResponse.Success<List<UpliftClass>>).data
    }

    val classesScrollState = rememberLazyListState()
    val selectedDayState = classesViewModel.selectedDay.collectAsState()

    // Needed in order to roughly center empty state in LazyColumn.
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    val titleText = classesViewModel.selectedDay.collectAsState().value.let { day ->
        if (day < -1) {
            "${day.absoluteValue} days ago"
        } else {
            when (day) {
                -1 -> "Yesterday"
                0 -> "Today"
                1 -> "Tomorrow"
                else -> "In $day days"
            }
        }
    }

    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            UpliftTopBar(showIcon = true, title = "Classes")

            LazyColumn(
                state = classesScrollState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
                    .zIndex(-1f)
            ) {
                stickyHeader {
                    Column(
                        modifier = Modifier
                            .background(
                                Brush.verticalGradient(
                                    Pair(.9f, Color.White), Pair(1f, Color.Transparent)
                                )
                            )
                            .padding(vertical = 14.5.dp)
                    ) {
                        CalendarBar(selectedDay = selectedDayState.value) { daySelected ->
                            classesViewModel.selectDay(daySelected)
                            // Scroll to the top when a new day is pressed.
                            coroutineScope.launch {
                                classesScrollState.animateScrollToItem(0)
                            }
                        }
                    }
                }
                item {
                    Text(
                        text = titleText.uppercase(),
                        fontFamily = montserratFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight(700),
                        color = PRIMARY_BLACK,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 14.dp),
                        textAlign = TextAlign.Center,
                    )
                }

                item {
                    if (classesList.isEmpty())
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height((screenHeightDp - 400).dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_green_tea),
                                contentDescription = null,
                                modifier = Modifier.size(width = 85.dp, height = 71.dp)
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = "No classes",
                                fontFamily = montserratFamily,
                                fontSize = 24.sp,
                                fontWeight = FontWeight(700),
                                color = PRIMARY_BLACK,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                            )
                            Text(
                                text = "Relax with some tea or play a sport!",
                                fontFamily = montserratFamily,
                                fontSize = 14.sp,
                                fontWeight = FontWeight(400),
                                color = PRIMARY_BLACK,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                            )
                        }
                }

                items(items = classesList) { myClass ->
                    ClassInfoCard(
                        thisClass = myClass,
                        navController = navController,
                        classDetailViewModel = classDetailViewModel
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .align(BottomCenter)
                .padding(bottom = 75.dp)
                .width(164.dp)
                .height(43.dp),
            shape = RoundedCornerShape(43.dp),
            colors = ButtonDefaults.buttonColors(Color.White)
        ) {
            Text(
                text = "APPLY FILTER",
                fontFamily = montserratFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight(700),
                color = PRIMARY_BLACK,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        }
    }
}
