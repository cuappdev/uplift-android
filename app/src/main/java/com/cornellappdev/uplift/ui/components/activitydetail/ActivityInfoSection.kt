package com.cornellappdev.uplift.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.TextStyle
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(5.dp))
        DayOfWeekSelector(selectedDay) { day ->
            selectedDay = day
        }
        Spacer(modifier = Modifier.height(14.dp))
        if (null == location.hours[selectedDay]) {
            Text("Closed")
        } else {
            for (interval in location.hours[selectedDay]!!) {
                Text(
                    text = interval.toString(),
                    fontFamily = montserratFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight(300),
                    lineHeight = 26.sp,
                    textAlign = TextAlign.Left,
                    modifier = Modifier.padding(bottom = 5.dp),
                    color = PRIMARY_BLACK
                )

            }
        }
        Spacer(Modifier.padding(bottom = 14.dp))

        Column(
            Modifier
                .border(
                    width = 1.dp,
                    color = Color(0xFFE5ECED),
                    shape = RoundedCornerShape(size = 8.dp)
                )
                .width(345.dp)
                .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(size = 8.dp))
                .padding(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 12.dp)
        ) {
            if (location.pricing == null) {
                Row(
                    Modifier
                        .width(313.dp),

                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Pricing",
                        fontFamily = montserratFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight(500),
                        lineHeight = 19.5.sp,
                        textAlign = TextAlign.Left,
                        modifier = Modifier
                            .width(54.dp)
                            .height(18.dp)

                    )
                    Text(
                        "Free", fontFamily = montserratFamily,
                        textAlign = TextAlign.Left,
                        fontWeight = FontWeight.Bold,
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            fontWeight = FontWeight(600),
                            color = Color(0xFF222222)
                        )
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
                        fontSize = 14.sp,
                        fontWeight = FontWeight(500),
                        lineHeight = 19.5.sp,
                        textAlign = TextAlign.Left
                    )
                    Text(
                        text = "Membership / Paid", fontFamily = montserratFamily,
                        textAlign = TextAlign.Left,
                        fontWeight = FontWeight(600),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(Modifier.fillMaxHeight()) {
                        for (priceOption in location.pricing) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    priceOption.first, fontFamily = montserratFamily,
                                    textAlign = TextAlign.Left
                                )
                                Text(
                                    "$ " + priceOption.second.toString(),
                                    fontFamily = montserratFamily,
                                    textAlign = TextAlign.Left,
                                    fontWeight = FontWeight(600)
                                )
                            }
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = Color(0xFFE5ECED),
                    shape = RoundedCornerShape(size = 8.dp)
                )
                .width(345.dp)
                .height(44.dp)
                .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(size = 8.dp))
                .padding(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        )
        {

            Text(
                text = "Group Reservations",
                fontFamily = montserratFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight(500),
                lineHeight = 19.5.sp,
                textAlign = TextAlign.Left
            )
            Text(
                text = "Required", fontFamily = montserratFamily,
                textAlign = TextAlign.Left,
                fontWeight = FontWeight(600)
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = Color(0xFFE5ECED),
                    shape = RoundedCornerShape(size = 8.dp)
                )
                .width(345.dp)
                .height(96.dp)
                .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(size = 8.dp))
                .padding(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 12.dp)
        ) {
            Text(
                text = "Gear for Rent",
                style = MaterialTheme.typography.h6,
                fontFamily = montserratFamily,
                fontSize = 14.sp,
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
                            textAlign = TextAlign.Left
                        )
                        Text(
                            "$ " + gear.second.toString(),
                            fontFamily = montserratFamily,
                            textAlign = TextAlign.Left,
                            fontWeight = FontWeight(600),
                        )
                    }
                }
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = Color(0xFFE5ECED),
                    shape = RoundedCornerShape(size = 8.dp)
                )
                .width(345.dp)
                .height(72.dp)
                .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(size = 8.dp))
                .padding(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 12.dp)
        ) {
            Text(
                text = "Services",
                fontFamily = montserratFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight(500),
                lineHeight = 19.5.sp,
                textAlign = TextAlign.Left,
            )

            Text(
                text = location.services.joinToString(", "),
                fontFamily = montserratFamily,
                textAlign = TextAlign.Left,
                fontWeight = FontWeight(400)
            )
        }

        Column(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = Color(0xFFE5ECED),
                    shape = RoundedCornerShape(size = 8.dp)
                )
                .width(345.dp)
                .height(70.dp)
                .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(size = 8.dp))
                .padding(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 12.dp)
        ) {
            Text(
                text = "Address",
                fontFamily = montserratFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight(500),
                lineHeight = 19.5.sp,
                textAlign = TextAlign.Left,
            )
            Text(
                text = location.address,
                fontFamily = montserratFamily,
                textAlign = TextAlign.Left,
                fontWeight = FontWeight(400)
            )
        }




        Button(
            onClick = { //map?
            },
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .shadow(elevation = 4.dp, clip = true),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.White,
                contentColor = Color.Black

            )
        ) {
            Text(text = "GET DIRECTIONS", fontSize = 14.sp)

        }


    }
}

