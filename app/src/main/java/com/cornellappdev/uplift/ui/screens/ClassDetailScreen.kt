package com.cornellappdev.uplift.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import coil.compose.AsyncImagePainter
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.models.UpliftClass
import com.cornellappdev.uplift.ui.components.classdetail.ClassDateAndTime
import com.cornellappdev.uplift.ui.components.classdetail.ClassDescription
import com.cornellappdev.uplift.ui.components.classdetail.ClassFunction
import com.cornellappdev.uplift.ui.components.classdetail.ClassPreparation
import com.cornellappdev.uplift.ui.components.classdetail.NextUpliftClassSessions
import com.cornellappdev.uplift.ui.components.general.FavoriteButton
import com.cornellappdev.uplift.ui.viewmodels.ClassDetailViewModel
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.GRAY03
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.bebasNeueFamily
import com.cornellappdev.uplift.util.colorInterp
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

    var loading by remember { mutableStateOf(true) }

    val infiniteTransition = rememberInfiniteTransition(label = "gymDetailLoading")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = .5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gymDetailLoading"
    )

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
                    }
                    .then(
                        if (loading) Modifier
                            .background(colorInterp(progress, GRAY01, GRAY03)) else Modifier
                    ),
                contentScale = ContentScale.Crop,
                onState = { state ->
                    loading = state !is AsyncImagePainter.State.Success
                }
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

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 40.dp, end = 21.dp)
            ) {
                FavoriteButton(
                    filled = (upliftClass != null && upliftClass!!.isFavorite())
                ) { upliftClass?.toggleFavorite() }
            }

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
        if (!upliftClass?.functions.isNullOrEmpty()) {
            ClassFunction(upliftClass = upliftClass)
            LineSpacer()
        }

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
            classDetailViewModel = classDetailViewModel,
            navController = navController
        )

        Spacer(Modifier.height(50.dp))
    }
}
