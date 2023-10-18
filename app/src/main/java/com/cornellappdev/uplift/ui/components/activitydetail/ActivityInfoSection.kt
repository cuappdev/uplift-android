package com.cornellappdev.uplift.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.models.UpliftActivity
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.montserratFamily

/**
 * Displays detailed activity information for the given gym throughout the week, defaulting to the day
 * given by parameter [today].
 */
@Composable
fun ActivityInfoSection(today: Int, location: UpliftActivity) {
    var selectedDay by remember {
        mutableStateOf(today)
    }

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(5.dp))
        DayOfWeekSelector(selectedDay) { day ->
            selectedDay = day
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (null == location.hours[selectedDay]) {
            Text("Closed")
        } else {
            for (interval in location.hours[selectedDay]!!) {
                Text(
                    text = interval.toString(),
                    fontFamily = montserratFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight(300),
                    lineHeight = 26.sp,
                    textAlign = TextAlign.Left,
                    modifier = Modifier.padding(bottom = 5.dp),
                    color = PRIMARY_BLACK
                )
            }
        }
        Spacer(Modifier.padding(bottom = 16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                if (location.pricing == null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Pricing",
                            fontFamily = montserratFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight(500),
                            lineHeight = 19.5.sp,
                            textAlign = TextAlign.Left
                        )
                        Text(
                            "Free", fontFamily = montserratFamily,
                            textAlign = TextAlign.Left,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Pricing",
                            fontFamily = montserratFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight(500),
                            lineHeight = 19.5.sp,
                            textAlign = TextAlign.Left
                        )
                        Text(
                            text = "Membership / Paid", fontFamily = montserratFamily,
                            textAlign = TextAlign.Left,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            for (priceOption in location.pricing) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        priceOption.first, fontFamily = montserratFamily,
                                        textAlign = TextAlign.Left,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                    Text(
                                        priceOption.second.toString(),
                                        fontFamily = montserratFamily,
                                        textAlign = TextAlign.Left,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                )
                {
                    Text(
                        text = "Group Reservations",
                        fontFamily = montserratFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight(500),
                        lineHeight = 19.5.sp,
                        textAlign = TextAlign.Left
                    )
                    Text(
                        text = "Required", fontFamily = montserratFamily,
                        textAlign = TextAlign.Left,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                Column {
                    Text(
                        text = "Gear for Rent",
                        style = MaterialTheme.typography.h6,
                        fontFamily = montserratFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight(500),
                        lineHeight = 19.5.sp,
                        textAlign = TextAlign.Left
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        for (gear in location.equipment!!) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    gear.first, fontFamily = montserratFamily,
                                    textAlign = TextAlign.Left,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                                Text(
                                    gear.second.toString(),
                                    fontFamily = montserratFamily,
                                    textAlign = TextAlign.Left,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }


                Text(
                    text = "Services",
                    fontFamily = montserratFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight(500),
                    lineHeight = 19.5.sp,
                    textAlign = TextAlign.Left,
                )

                Text(
                    text = location.services.joinToString(", "),
                    fontFamily = montserratFamily,
                    textAlign = TextAlign.Left,
                    modifier = Modifier.padding(start = 8.dp)
                )

                Text(
                    text = "Address",
                    fontFamily = montserratFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight(500),
                    lineHeight = 19.5.sp,
                    textAlign = TextAlign.Left,
                )

                Text(
                    text = location.address,
                    fontFamily = montserratFamily,
                    textAlign = TextAlign.Left,
                    modifier = Modifier.padding(start = 8.dp)
                )


                Button(
                    onClick = { //map?
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)
                        .shadow(elevation = 4.dp, clip = true),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {
                    Text(text = "GET DIRECTIONS")

                }
            }

        }

    }
}

