package com.cornellappdev.uplift.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.cornellappdev.uplift.models.UpliftClass
import com.cornellappdev.uplift.ui.components.classdetail.*
import com.cornellappdev.uplift.ui.viewmodels.ClassDetailViewModel
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.bebasNeueFamily
import com.cornellappdev.uplift.util.montserratFamily


/**
 * A screen that displays details for a given [UpliftClass].
 */
@Composable
fun ClassDetailScreen(
    classDetailViewModel: ClassDetailViewModel = viewModel(),
    navController: NavHostController,
    onBack: () -> Unit
) {
    val upliftClass by classDetailViewModel.classFlow.collectAsState()

    val scrollState = rememberScrollState()

    val screenDensity = LocalConfiguration.current.densityDpi / 160f
    val screenHeightPx = LocalConfiguration.current.screenHeightDp.toFloat() * screenDensity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // Top Part
        Box(modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                translationY = 0.5f * scrollState.value
            }
        ) {
            AsyncImage(
                model = upliftClass?.imageUrl,
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
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = onBack
                    ),
                tint = Color.White
            )
            Image(
                painter = painterResource(id = if (upliftClass != null && upliftClass!!.isFavorite()) R.drawable.ic_star_filled else R.drawable.ic_star),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 47.dp, end = 21.dp)
                    .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                        upliftClass?.toggleFavorite()
                    }
            )
            // All text header information...
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = 42.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = upliftClass?.name?.uppercase() ?: "",
                    fontWeight = FontWeight(700),
                    fontSize = 48.sp,
                    lineHeight = 48.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontFamily = bebasNeueFamily
                )
                Text(
                    text = upliftClass?.location ?: "",
                    fontWeight = FontWeight(400),
                    fontSize = 14.sp,
                    lineHeight = 17.07.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontFamily = montserratFamily
                )
                Spacer(Modifier.height(24.dp))
                Text(
                    text = upliftClass?.instructorName ?: "",
                    fontWeight = FontWeight(700),
                    fontSize = 16.sp,
                    lineHeight = 19.5.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontFamily = montserratFamily
                )
            }

            Surface(
                shape = CircleShape,
                color = Color.White,
                modifier = Modifier
                    .size(84.dp)
                    .offset(y = 42.dp)
                    .align(Alignment.BottomCenter)
                    .graphicsLayer {
                        translationY = -0.5f * scrollState.value.toFloat()
                    }
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "${upliftClass?.time?.durationMinutes()} MIN",
                        fontWeight = FontWeight(700),
                        fontSize = 14.sp,
                        lineHeight = 17.07.sp,
                        textAlign = TextAlign.Center,
                        color = PRIMARY_BLACK,
                        fontFamily = montserratFamily,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .offset(y = 19.dp)
                    )
                }
            }
        }

        // Date & Time Information
        ClassDateAndTime(upliftClass = upliftClass)

        LineSpacer()

        // Function
        ClassFunction(upliftClass = upliftClass)

        LineSpacer()

        // Preparation
        if (!upliftClass?.preparation.isNullOrEmpty()) {
            ClassPreparation(upliftClass = upliftClass)
            LineSpacer()
        }

        // Description
        ClassDescription(upliftClass = upliftClass)

        LineSpacer()

        // Next Sessions
        NextUpliftClassSessions(
            upliftClass = upliftClass,
            classDetailViewModel = classDetailViewModel,
            navController = navController
        )

        Spacer(Modifier.height(50.dp))
    }
}