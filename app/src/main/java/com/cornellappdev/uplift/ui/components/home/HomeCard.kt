package com.cornellappdev.uplift.ui.components.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.res.colorResource
import coil.compose.AsyncImage
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.models.*
import com.cornellappdev.uplift.util.*
import java.util.*

/**
 * Parameters: Gym Class
 * Builds Homecard for gym class with picture, timings, current availability.
 * Each gym has the ability to be starred and added to users liked gyms.
 */

@Composable
fun HomeCard(gymexample: Gym) {

    val day = ((Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2) + 7) % 7
    val lastTime =
        gymexample.hours.get(day)!!.get((gymexample.hours.get(day)!!.size - 1)).end.toString()
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .fillMaxWidth()
            .aspectRatio(2F / 1F)
    )
    {
        Card(
            shape = RoundedCornerShape(12.dp)
        ) {
            Column() {
                Box(modifier = Modifier.weight(3F)) {
                    AsyncImage(
                        model = gymexample.imageUrl,
                        modifier = Modifier.fillMaxWidth(),
                        contentDescription = "",
                        contentScale = ContentScale.FillBounds
                    )
                    Image(
                        painterResource(id = if (gymexample.isFavorite()) R.drawable.ic_star_filled else R.drawable.ic_star),
                        contentDescription = "Star Icon",
                        modifier = Modifier
                            // .size(24.dp)
                            .align(TopEnd)
                            .padding(top = 12.dp, end = 12.dp)
                            .align(TopEnd)
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null
                            ) {
                                gymexample.toggleFavorite()
                            }

                    )
                }
                Column(modifier = Modifier.weight(2F)) {
                    Row()
                    {
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = gymexample.name,
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
                        )
                        Image(
                            painter = painterResource(id = R.drawable.ic_bowling_pins),
                            contentDescription = "Bowling Pins",
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Spacer(Modifier.height(4.dp))
                    }
                    Row() {
                        Spacer(Modifier.width(12.dp))
                        if (determineOpen(gymexample.hours)) Text(
                            text = "Open",
                            fontSize = 12.sp,
                            color = Color.Green,

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
                            text = "Closes at " + lastTime,
                            fontSize = 12.sp,
                            fontFamily = montserratFamily,
                            fontWeight = FontWeight(500),
                        )

                    }
                    Row() {
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
                            modifier = Modifier.padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.weight(1F))
                        Text(
                            text = "1.2mi",
                            fontSize = 12.sp,
                            fontFamily = montserratFamily,
                            fontWeight = FontWeight(500),
                            modifier = Modifier.padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                }
            }
        }

    }
}
fun determineOpen(listTime: List<List<TimeInterval>?>): Boolean {
    for (i in 0..listTime.size - 1) {
        if (isCurrentlyOpen(listTime.get(i)!!)) {
            return true;
        }
    }
    return false;
}

