package com.cornellappdev.uplift.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.cornellappdev.uplift.ui.components.ClassInfoCard
import com.cornellappdev.uplift.ui.components.general.UpliftTopBar
import com.cornellappdev.uplift.ui.viewmodels.ClassDetailViewModel
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.exampleClassMusclePump1
import com.cornellappdev.uplift.util.exampleClassMusclePump2
import com.cornellappdev.uplift.util.montserratFamily

/**
 * Parameters: classDetailViewModel
 * Builds ClassScreen with list of available classes and button to filtering screen.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClassScreen(
    classDetailViewModel: ClassDetailViewModel = viewModel(),
    navController: NavHostController
) {
    val classesList = listOf(
        exampleClassMusclePump1,
        exampleClassMusclePump2,
        exampleClassMusclePump1,
        exampleClassMusclePump2,
        exampleClassMusclePump1,
        exampleClassMusclePump2
    )
    val classesScrollState = rememberLazyListState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            UpliftTopBar(showIcon = true, title = "Classes")

            LazyColumn(
                state = classesScrollState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 20.dp)
                    .zIndex(-1f)
            ) {
                stickyHeader {
                    Text(
                        text = "Example Header :-)",
                        fontFamily = montserratFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight(700),
                        color = PRIMARY_BLACK,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    Pair(.9f, Color.White), Pair(1f, Color.Transparent)
                                )
                            )
                            .padding(top = 24.dp, bottom = 24.dp),
                        textAlign = TextAlign.Center,
                    )
                }
                item {
                    Text(
                        text = "TODAY",
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

                items(items = classesList) { classesList ->
                    ClassInfoCard(
                        thisClass = classesList,
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
