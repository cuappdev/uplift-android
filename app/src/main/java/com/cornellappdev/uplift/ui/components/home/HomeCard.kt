package com.cornellappdev.uplift.ui.components.home

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.data.models.gymdetail.UpliftGym
import com.cornellappdev.uplift.data.models.ApiResponse
import com.cornellappdev.uplift.data.repositories.CoilRepository
import com.cornellappdev.uplift.ui.components.general.FavoriteButton
import com.cornellappdev.uplift.util.*


/**
 * Parameters: Gym Class
 * Builds Home card for gym class with picture, timings, current availability.
 * Each gym has the ability to be starred and added to users liked gyms.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeCard(gym: UpliftGym, onClick: () -> Unit) {
    val day: Int = todayIndex()

    // Gets the current time interval's ending time.
    val activeInterval = gym.hours[day]?.find { hour -> hour.within(getSystemTime()) }
    val closesAtTime = activeInterval?.end?.toString()

    // The next time the gym will open today, if ever.
    val nextInterval = gym.hours[day]?.find { hour -> hour.start.compareTo(getSystemTime()) > 0 }
    val opensAtTime = nextInterval?.start?.toString()

    val bitmapState = CoilRepository.getUrlState(gym.imageUrl, LocalContext.current)
    val infiniteTransition = rememberInfiniteTransition(label = "homeCardLoading")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = .5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "homeCardLoading"
    )
    val showGymCapacity = gym.upliftCapacity != null && isOpen(gym.hours[day])

    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 12.dp)
            .fillMaxWidth()
            .aspectRatio(2F / 1F)
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            backgroundColor = Color.White,
            onClick = onClick
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(if (isOpen(gym.hours[day])) 1f else .6f)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Crossfade(
                        targetState = bitmapState.value,
                        label = "imageFade",
                        animationSpec = tween(250)
                    ) { apiResponse ->
                        when (apiResponse) {
                            is ApiResponse.Success ->
                                Image(
                                    bitmap = apiResponse.data,
                                    modifier = Modifier.fillMaxWidth(),
                                    contentDescription = "",
                                    contentScale = ContentScale.Crop
                                )

                            else ->
                                Image(
                                    bitmap = ImageBitmap(width = 1, height = 1),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(colorInterp(progress, GRAY01, GRAY03)),
                                    contentDescription = "",
                                    contentScale = ContentScale.Crop
                                )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .align(TopEnd)
                            .padding(top = 12.dp, end = 12.dp)
                    ) {
                        FavoriteButton(filled = gym.isFavorite()) {
                            gym.toggleFavorite()
                        }
                    }

                    Column(
                        modifier = Modifier
                            .align(BottomCenter)
                            .background(Color.White)
                            .padding(top = 8.dp, start = 12.dp, end = 12.dp, bottom = 12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.Top) {
                            Text(
                                text = gym.name,
                                fontSize = 16.sp,
                                fontFamily = montserratFamily,
                                fontWeight = FontWeight(500),
                                lineHeight = 19.5.sp,
                                color = PRIMARY_BLACK
                            )
                            Spacer(modifier = Modifier.weight(1F))
                            Image(
                                painter = painterResource(id = R.drawable.ic_dumbbell),
                                contentDescription = "Dumbbell",
                                colorFilter = ColorFilter.tint(color = GRAY04)
                            )
                            if (gym.bowlingInfo != null) {
                                Spacer(Modifier.width(4.dp))
                                Image(
                                    painter = painterResource(id = R.drawable.ic_bowling_pins),
                                    contentDescription = "Bowling Pins",
                                    colorFilter = ColorFilter.tint(color = GRAY04)
                                )
                            }
                            if (gym.swimmingInfo != null) {
                                Spacer(Modifier.width(4.dp))
                                Image(
                                    painter = painterResource(id = R.drawable.ic_swimming_pool),
                                    contentDescription = "Swimming Pool",
                                    colorFilter = ColorFilter.tint(color = GRAY04)
                                )
                            }
                            if (gym.courtInfo.isNotEmpty()) {
                                Spacer(Modifier.width(4.dp))
                                Image(
                                    painter = painterResource(id = R.drawable.ic_basketball_hoop),
                                    contentDescription = "Gymnasium Basketball Hoop",
                                    colorFilter = ColorFilter.tint(color = GRAY04)
                                )
                            }
                        }
                        Row {
                            if (isOpen(gym.hours[day])) Text(
                                text = "Open",
                                fontSize = 12.sp,
                                color = ACCENT_OPEN,
                                lineHeight = 14.63.sp,
                                fontFamily = montserratFamily,
                                fontWeight = FontWeight(500),
                            )
                            else Text(
                                text = "Closed",
                                fontSize = 12.sp,
                                color = Color.Red,
                                lineHeight = 14.63.sp,
                                fontFamily = montserratFamily,
                                fontWeight = FontWeight(500),
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (closesAtTime != null) "Closes at $closesAtTime"
                                else if (opensAtTime != null) "Opens at $opensAtTime"
                                else "Closed today",
                                fontSize = 12.sp,
                                fontFamily = montserratFamily,
                                fontWeight = FontWeight(500),
                                lineHeight = 14.63.sp,
                                color = GRAY03
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            if (!showGymCapacity && gym.getDistance() != null)
                                Text(
                                    text = "${String.format("%.1f", gym.getDistance())}mi",
                                    fontSize = 12.sp,
                                    fontFamily = montserratFamily,
                                    fontWeight = FontWeight(500),
                                    lineHeight = 14.63.sp,
                                    color = GRAY03
                                )
                        }
                        Row(modifier = Modifier.padding(top = 2.dp)) {
                            if (showGymCapacity) {
                                Text(
                                    text = gym.upliftCapacity!!.percent.let { perc ->
                                        if (perc <= .5) "Light"
                                        else if (perc <= .8) "Cramped"
                                        else "Full"
                                    },
                                    fontSize = 12.sp,
                                    fontFamily = montserratFamily,
                                    fontWeight = FontWeight(500),
                                    color = gym.upliftCapacity.percent.let { perc ->
                                        if (perc <= .5) ACCENT_OPEN
                                        else if (perc <= .8) ACCENT_ORANGE
                                        else ACCENT_CLOSED
                                    },
                                    lineHeight = 14.63.sp,
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = gym.upliftCapacity.percentString(),
                                    fontSize = 12.sp,
                                    fontFamily = montserratFamily,
                                    fontWeight = FontWeight(500),
                                    lineHeight = 14.63.sp,
                                    color = GRAY03
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                if (gym.getDistance() != null)
                                    Text(
                                        text = "${String.format("%.1f", gym.getDistance())}mi",
                                        fontSize = 12.sp,
                                        fontFamily = montserratFamily,
                                        fontWeight = FontWeight(500),
                                        lineHeight = 14.63.sp,
                                        color = GRAY03
                                    )
                            }
                        }
                    }
                }
            }
        }

    }
}
