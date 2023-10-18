package com.cornellappdev.uplift.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.viewmodels.ActivityDetailViewModel
import com.cornellappdev.uplift.ui.viewmodels.ClassDetailViewModel
import com.cornellappdev.uplift.util.*

/**
 * A screen displaying all the information about a selected activity.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ActivityDetailScreen(
    activityDetailViewModel: ActivityDetailViewModel = viewModel(),
    classDetailViewModel: ClassDetailViewModel,
    navController: NavHostController,
    onBack: () -> Unit
) {
    val activity by activityDetailViewModel.activityFlow.collectAsState()
    val day = todayIndex()

    val scrollState = rememberScrollState()

    val screenDensity = LocalConfiguration.current.densityDpi / 160f
    val screenHeightPx = LocalConfiguration.current.screenHeightDp.toFloat() * screenDensity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color.White)
    ) {
        // Top Part
        Box(modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                translationY = 0.5f * scrollState.value
            })
        {
            AsyncImage(
                model = activity?.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .graphicsLayer {
                        alpha = 1 - (scrollState.value.toFloat() / screenHeightPx)
                    },
                contentScale = ContentScale.Crop
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_back_arrow),
                contentDescription = null,
                modifier = Modifier
                    .align(
                        Alignment.TopStart
                    )
                    .padding(top = 47.dp, start = 22.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = onBack
                    ),
                tint = Color.White
            )

//            Box(
//                modifier = Modifier
//                    .align(Alignment.TopEnd)
//                    .padding(top = 47.dp, end = 21.dp)
//            ) {
//                FavoriteButton(
//                    filled = (activity != null && activity!!.isFavorite())
//                ) { activity?.toggleFavorite() }
//            }

            Text(
                modifier = Modifier.align(Alignment.Center),
                text = activity?.name?.uppercase() ?: "",
                fontWeight = FontWeight(700),
                fontSize = 36.sp,
                lineHeight = 44.sp,
                textAlign = TextAlign.Center,
                letterSpacing = 1.13.sp,
                color = Color.White,
                fontFamily = montserratFamily
            )

            // CLOSED
            if (activity != null && (activity!!.hours[day] == null || !isCurrentlyOpen(activity!!.hours[day]!!))) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .graphicsLayer {
                            translationY = -0.5f * scrollState.value
                        }
                        .background(PRIMARY_BLACK)
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    Text(
                        text = "CLOSED",
                        fontWeight = FontWeight(500),
                        fontSize = 16.sp,
                        lineHeight = 19.5.sp,
                        textAlign = TextAlign.Center,
                        letterSpacing = 3.sp,
                        color = Color.White,
                        fontFamily = montserratFamily,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

/** add the activiti detail
//        if (activity != null) {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(Color.White)
//            ) {
//                GymHours(hours = activity!!.hours, day)
//                LineSpacer()
//                PopularTimesSection(activity!!.popularTimes[day])
//                LineSpacer()
//                GymFacilitySection(activity!!, day)
//                GymTodaysClasses(
//                    gymDetailViewModel = activityDetailViewModel,
//                    classDetailViewModel = classDetailViewModel,
//                    navController = navController
//                )
//            }
//        }

//        Spacer(Modifier.height(50.dp))
//    }
 */