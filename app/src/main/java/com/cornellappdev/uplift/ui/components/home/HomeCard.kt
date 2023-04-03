package com.cornellappdev.uplift.ui.components.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
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
import com.cornellappdev.uplift.models.Gym
import com.cornellappdev.uplift.util.*
import java.util.*


/**
 * Parameters: Gym Class
 * Builds Home card for gym class with picture, timings, current availability.
 * Each gym has the ability to be starred and added to users liked gyms.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeCard(gym: Gym, onClick: () -> Unit) {
    val day: Int = ((Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2) + 7) % 7
    val lastTime =
        gym.hours[day]!![(gym.hours[day]!!.size - 1)].end.toString()

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
            Column {
                Box(modifier = Modifier.weight(3F)) {
                    AsyncImage(
                        model = gym.imageUrl,
                        modifier = Modifier.fillMaxWidth(),
                        contentDescription = "",
                        contentScale = ContentScale.Crop
                    )
                    Image(
                        painterResource(id = if (gym.isFavorite()) R.drawable.ic_star_filled else R.drawable.ic_star),
                        contentDescription = "Star Icon",
                        modifier = Modifier
                            .align(TopEnd)
                            .padding(top = 12.dp, end = 12.dp)
                            .align(TopEnd)
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null
                            ) {
                                gym.toggleFavorite()
                            }

                    )
                }
                Column(modifier = Modifier.weight(2F)) {
                    Row(modifier = Modifier.padding(top = 8.dp)) {
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = gym.name,
                            fontSize = 16.sp,
                            fontFamily = montserratFamily,
                            fontWeight = FontWeight(500)

                        )
                        Spacer(
                            modifier = Modifier.weight(1F),
                        )
                        Image(
                            painter = painterResource(id = R.drawable.ic_dumbbell),
                            contentDescription = "Dumbell",
                            colorFilter = ColorFilter.tint(color = GRAY04)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.ic_bowling_pins),
                            contentDescription = "Bowling Pins",
                            colorFilter = ColorFilter.tint(color = GRAY04)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Spacer(Modifier.height(4.dp))
                    }
                    Row {
                        Spacer(Modifier.width(12.dp))
                        if (isCurrentlyOpen(gym.hours[day]!!)) Text(
                            text = "Open",
                            fontSize = 12.sp,
                            color = ACCENT_OPEN,

                            fontFamily = montserratFamily,
                            fontWeight = FontWeight(500),
                        )
                        else Text(
                            text = "Closed",
                            fontSize = 12.sp,
                            color = Color.Red,

                            fontFamily = montserratFamily,
                            fontWeight = FontWeight(500),
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        /*
                        @Todo Make this dynamic with networking instead of hardcoding info.
                         */
                        Text(
                            text = "Closes at $lastTime",
                            fontSize = 12.sp,
                            fontFamily = montserratFamily,
                            fontWeight = FontWeight(500),
                            color = GRAY03

                        )

                    }
                    Row {
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = "Cramped",
                            fontSize = 12.sp,
                            fontFamily = montserratFamily,
                            fontWeight = FontWeight(500),
                            color = colorResource(id = R.color.orange),
                            modifier = Modifier.padding(top = 2.dp),
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = " 120/140",
                            fontSize = 12.sp,
                            fontFamily = montserratFamily,
                            fontWeight = FontWeight(500),
                            modifier = Modifier.padding(top = 2.dp),
                            color = GRAY03
                        )
                        Spacer(modifier = Modifier.weight(1F))
                        Text(
                            text = "1.2mi",
                            fontSize = 12.sp,
                            fontFamily = montserratFamily,
                            fontWeight = FontWeight(500),
                            modifier = Modifier.padding(top = 2.dp),
                            color = GRAY03

                        )
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                }
            }
        }

    }
}


