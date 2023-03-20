package com.cornellappdev.uplift.ui.components.Home
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.models.Gym
import com.cornellappdev.uplift.util.montserratFamily


@Composable
    fun homeCard(gymexample:Gym ) {
    var paintres = remember  {mutableStateOf(if(gymexample.isFavorite()) R.drawable.ic_star_filled else R.drawable.ic_star) }
        Box(modifier= Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .fillMaxHeight()
            .aspectRatio(2F / 1F)){
            Card(
                modifier = Modifier
            ) {
                Column() {
                    Column(modifier = Modifier.weight(3F)) {
                        Image(
                            modifier = Modifier.fillMaxWidth(),
                            painter = painterResource(id=R.drawable.court_lines),
                            contentDescription = "Gym Image",
                            contentScale = ContentScale.FillWidth
                        )

                    }
                    Column(modifier = Modifier.weight(2F)) {
                        Row(
                            modifier = Modifier.padding(5.dp)
                        ) {
                            Text(
                                text =gymexample.name,
                                fontSize = 20.sp,
                                fontFamily = montserratFamily

                                )
                            Spacer(modifier = Modifier.weight(1F))
                            Image(
                                painter = painterResource(id = R.drawable.ic_dumbbell),
                                contentDescription = "Dumbell",
                            )
                            Image(
                                painter = painterResource(id = R.drawable.ic_bowling_pins),
                                contentDescription = "Bowling Pins",
                            )

                        }
                        Row(modifier = Modifier.padding(start = 5.dp, end = 5.dp)) {
                            Text(
                                text = "Open",
                                fontSize = 12.sp,
                                color = Color.Green,
                                fontFamily = montserratFamily

                                )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Closes at 6 pm",
                                fontSize = 12.sp,
                                fontFamily = montserratFamily
                                )

                        }
                        Row(
                            modifier = Modifier.padding(
                                start = 5.dp,
                                end = 5.dp
                            )
                        ) {
                            Text(
                                text = "Cramped",
                                fontSize = 12.sp,
                                color = Color.Red,
                                fontFamily = montserratFamily
                                )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = " 120/140",
                                fontSize = 12.sp,
                                fontFamily = montserratFamily
                            )
                            Spacer(modifier = Modifier.weight(1F))
                            Text(
                                text="1.2mi",
                                fontFamily = montserratFamily
                            )

                        }
                    }
                }
            }
            /**
             * Changes Icon image to either be filled star or regular star depending on if the user has liked
             * that particular gym.
              */
            IconButton(
                onClick = {
                    gymexample.toggleFavorite() },
                modifier = Modifier
                    .size(24.dp)
                    .align(TopEnd),
            ) {
                Icon(
                    painterResource(id=paintres.value),
                    contentDescription="Star Icon"

                )
            }
        }
    }

