package com.cornellappdev.uplift.ui.screens

import android.content.Intent
import android.provider.CalendarContract
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.models.UpliftClass
import com.cornellappdev.uplift.ui.components.ClassInfoCard
import com.cornellappdev.uplift.ui.viewmodels.ClassDetailViewModel
import com.cornellappdev.uplift.ui.viewmodels.HomeViewModel
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.bebasNeueFamily
import com.cornellappdev.uplift.util.montserratFamily


/**
 * A screen that displays details for a given [UpliftClass].
 */
@Composable
fun ClassDetailScreen(
    classDetailViewModel: ClassDetailViewModel = viewModel(),
    homeViewModel: HomeViewModel = viewModel(),
    navController: NavHostController,
    onBack: () -> Unit
) {
    val upliftClass by classDetailViewModel.classFlow.collectAsState()
    val context = LocalContext.current

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
                    .clickable(onClick = onBack),
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
                        alpha = 2f
                        translationY = -0.5f * scrollState.value.toFloat()
                    }
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "${upliftClass?.minutes} MIN",
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))
            Text(
                text = upliftClass?.dateAsString() ?: "",
                fontWeight = FontWeight(300),
                fontSize = 16.sp,
                lineHeight = 19.5.sp,
                textAlign = TextAlign.Center,
                color = PRIMARY_BLACK,
                fontFamily = montserratFamily
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = upliftClass?.time.toString(),
                fontWeight = FontWeight(500),
                fontSize = 16.sp,
                lineHeight = 19.5.sp,
                textAlign = TextAlign.Center,
                color = PRIMARY_BLACK,
                fontFamily = montserratFamily
            )
            Spacer(Modifier.height(24.dp))
            Column(
                modifier = Modifier
                    .clickable {
                        upliftClass?.let {
                            val intent = Intent(Intent.ACTION_EDIT)
                            intent.type = "vnd.android.cursor.item/event"
                            intent.putExtra("beginTime", it.time.start.timeInMillis(it.date))
                            intent.putExtra("allDay", false)
                            intent.putExtra("endTime", it.time.end.timeInMillis(it.date))
                            intent.putExtra(CalendarContract.Events.EVENT_LOCATION, it.location)
                            intent.putExtra("title", it.name)
                            startActivity(context, intent, null)
                        }
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_calendar),
                    contentDescription = "Add to calendar",
                    tint = PRIMARY_BLACK
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "ADD TO CALENDAR",
                    fontWeight = FontWeight(700),
                    fontSize = 12.sp,
                    lineHeight = 14.63.sp,
                    textAlign = TextAlign.Center,
                    color = PRIMARY_BLACK,
                    fontFamily = montserratFamily
                )
            }
            Spacer(Modifier.height(24.dp))
        }

        LineSpacer()

        // Function
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))
            Text(
                text = "FUNCTION",
                fontWeight = FontWeight(700),
                fontSize = 16.sp,
                lineHeight = 19.5.sp,
                textAlign = TextAlign.Center,
                color = PRIMARY_BLACK,
                fontFamily = montserratFamily
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = upliftClass?.functionsAsString() ?: "",
                fontWeight = FontWeight(300),
                fontSize = 14.sp,
                lineHeight = 17.07.sp,
                textAlign = TextAlign.Center,
                color = PRIMARY_BLACK,
                fontFamily = montserratFamily,
                modifier = Modifier.padding(horizontal = 48.dp)
            )
            Spacer(Modifier.height(24.dp))
        }

        LineSpacer()

        // Preparation
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))
            Text(
                text = "PREPARATION",
                fontWeight = FontWeight(700),
                fontSize = 16.sp,
                lineHeight = 19.5.sp,
                textAlign = TextAlign.Center,
                color = PRIMARY_BLACK,
                fontFamily = montserratFamily
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = upliftClass?.preparation ?: "",
                fontWeight = FontWeight(300),
                fontSize = 14.sp,
                lineHeight = 17.07.sp,
                textAlign = TextAlign.Center,
                color = PRIMARY_BLACK,
                fontFamily = montserratFamily,
                modifier = Modifier.padding(horizontal = 48.dp)
            )
            Spacer(Modifier.height(24.dp))
        }

        LineSpacer()

        // Description
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))
            Text(
                text = upliftClass?.description ?: "",
                fontWeight = FontWeight(300),
                fontSize = 14.sp,
                lineHeight = 17.07.sp,
                textAlign = TextAlign.Center,
                color = PRIMARY_BLACK,
                fontFamily = montserratFamily,
                modifier = Modifier.padding(horizontal = 48.dp)
            )
            Spacer(Modifier.height(24.dp))
        }

        LineSpacer()

        // Next Sessions
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))
            Text(
                text = "NEXT SESSIONS",
                fontWeight = FontWeight(700),
                fontSize = 16.sp,
                lineHeight = 19.5.sp,
                textAlign = TextAlign.Center,
                color = PRIMARY_BLACK,
                fontFamily = montserratFamily
            )
            Spacer(Modifier.height(24.dp))
            for (nextClass: UpliftClass in upliftClass?.nextSessions ?: listOf()) {
                ClassInfoCard(
                    thisClass = nextClass,
                    navController = navController,
                    classDetailViewModel = classDetailViewModel
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            Spacer(Modifier.height(36.dp))
        }
    }
}