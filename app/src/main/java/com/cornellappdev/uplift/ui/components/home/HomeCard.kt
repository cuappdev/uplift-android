package com.cornellappdev.uplift.ui.components.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.models.UpliftGym
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
    val lastTime =
        if (gym.hours[day] != null) {
            gym.hours[day]!![(gym.hours[day]!!.size - 1)].end.toString()
        } else {
            null
        }

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
                    .alpha(if (isCurrentlyOpen(gym.hours[day])) 1f else .6f)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = gym.imageUrl,
                        modifier = Modifier.fillMaxWidth(),
                        contentDescription = "",
                        contentScale = ContentScale.Crop
                    )

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
                        Row {
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
                            if (gym.gymnasiumInfo != null) {
                                Spacer(Modifier.width(4.dp))
                                Image(
                                    painter = painterResource(id = R.drawable.ic_basketball_hoop),
                                    contentDescription = "Gymnasium Basketball Hoop",
                                    colorFilter = ColorFilter.tint(color = GRAY04)
                                )
                            }
                        }
                        Row {
                            if (isCurrentlyOpen(gym.hours[day])) Text(
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
                                text = if (lastTime != null) "Closes at $lastTime" else "Closed today",
                                fontSize = 12.sp,
                                fontFamily = montserratFamily,
                                fontWeight = FontWeight(500),
                                lineHeight = 14.63.sp,
                                color = GRAY03

                            )
                        }
                        Row(modifier = Modifier.padding(top = 2.dp)) {
                            Text(
                                text = "Cramped",
                                fontSize = 12.sp,
                                fontFamily = montserratFamily,
                                fontWeight = FontWeight(500),
                                color = colorResource(id = R.color.orange),
                                lineHeight = 14.63.sp,
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${gym.capacity.capacityPair.first}/${gym.capacity.capacityPair.second}",
                                fontSize = 12.sp,
                                fontFamily = montserratFamily,
                                fontWeight = FontWeight(500),
                                lineHeight = 14.63.sp,
                                color = GRAY03
                            )
                            Spacer(modifier = Modifier.weight(1F))
                            Text(
                                text = "1.2mi",
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
