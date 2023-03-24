package com.cornellappdev.uplift.ui.components.home
import androidx.compose.foundation.Image
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
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.ui.Alignment.Companion.TopEnd
import coil.compose.AsyncImage
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.models.*
import com.cornellappdev.uplift.util.*

/**
 * Parameters: Gym Class
 * Builds Homecard for gym class with picture, timings, current availability.
 * Each gym has the ability to be starred and added to users liked gyms.
 */

 @Composable
    fun HomeCard(gymexample:Gym ) {
    var paintres = if (gymexample.isFavorite()) R.drawable.ic_star_filled else R.drawable.ic_star
    var textVal = if(determineOpen(gymexample.hours)) Text(
        text = "Open",
        fontSize = 12.sp,
        color = Color.Green,

        fontFamily = montserratFamily,
        fontWeight= FontWeight(500),
        modifier = Modifier.padding(start = 12.dp,
            end =315.dp,
            top= 32.dp,
            bottom= 29.dp
        )
    )
         else Text(
            text = "Closed",
    fontSize = 12.sp,
    color = Color.Red,

    fontFamily = montserratFamily,
    fontWeight= FontWeight(500),
    modifier = Modifier.padding(start = 12.dp,
        end =315.dp,
        top= 32.dp,
        bottom= 29.dp
    )
    )
    Box(modifier= Modifier
            .padding(20.dp)
            .fillMaxWidth()
            .aspectRatio(2F / 1F))
            {
            Card(
                shape=RoundedCornerShape(12.dp)
            ){
                Column() {
                        AsyncImage(
                            model = gymexample.imageUrl,
                            modifier = Modifier.fillMaxWidth()
                                .weight(3F),
                            contentDescription = "Gym Image",
                            contentScale = ContentScale.FillBounds
                        )
                    Column(modifier = Modifier.weight(2F)) {
                        Row()
                        {
                            Spacer(Modifier.width(12.dp))
                            Text(
                                text =gymexample.name,
                                fontSize = 16.sp,
                                fontFamily = montserratFamily,
                                fontWeight= FontWeight(500)

                                )
                            Spacer(modifier = Modifier.weight(1F),
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
                            Text(
                                    text = "Open",
                                    fontSize = 12.sp,
                                    color = Color.Green,

                                fontFamily = montserratFamily,
                                fontWeight= FontWeight(500),
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            /*
                            @Todo Make this dynamic with networking instead of hardcoding info.
                             */
                            Text(
                                text = "Closes at 6 pm",
                                fontSize = 12.sp,
                                fontFamily = montserratFamily,
                                fontWeight= FontWeight(500),
                            )

                        }
                        Row() {
                            Spacer(Modifier.width(12.dp))
                            Text(
                                text = "Cramped",
                                fontSize = 12.sp,
                                color = Color.Red,
                                fontFamily = montserratFamily,
                                fontWeight= FontWeight(500),
                                modifier = Modifier.padding(top=2.dp)

                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = " 120/140",
                                fontSize = 12.sp,
                                fontFamily = montserratFamily,
                                fontWeight= FontWeight(500),
                                modifier = Modifier.padding(top=2.dp)
                            )
                            Spacer(modifier = Modifier.weight(1F))
                            Text(
                                text="1.2mi",
                                fontSize= 12.sp,
                                fontFamily = montserratFamily,
                                fontWeight= FontWeight(500),
                                modifier = Modifier.padding(top=2.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                        }
                    }
                }
            }
            //Changes Icon image to either be filled star or regular star depending on if the user has liked
            // that particular gym.
            IconButton(
                onClick = {
                    gymexample.toggleFavorite() },
                modifier = Modifier
                    .size(24.dp)
                    .align(TopEnd)
                    .padding(12.dp)

            ) {
                Icon(
                    painterResource(id=paintres),
                    contentDescription="Star Icon"

                )
            }
        }
    }
@Composable
@Preview
fun takePrev(){

    HomeCard(testMorrison)
}

fun determineOpen(listTime: List<List<TimeInterval>?>): Boolean{
    for (i in 0.. listTime.size-1){
        if (isCurrentlyOpen(listTime.get(i)!!)){
            return true;
        }
    }
    return false;
}
val testMorrison = Gym(
    name = "Toni Morrison Gym",
    hours = listOf(
        listOf(
            TimeInterval(
                TimeOfDay(hour = 2, minute = 0, isAM = false),
                TimeOfDay(hour = 11, minute = 0, isAM = false),
            )
        ),
        listOf(
            TimeInterval(
                TimeOfDay(hour = 2, minute = 0, isAM = false),
                TimeOfDay(hour = 11, minute = 0, isAM = false),
            )
        ),
        listOf(
            TimeInterval(
                TimeOfDay(hour = 2, minute = 0, isAM = false),
                TimeOfDay(hour = 4, minute = 0, isAM = false),
            ),
            TimeInterval(
                TimeOfDay(hour = 6, minute = 0, isAM = false),
                TimeOfDay(hour = 11, minute = 0, isAM = false),
            )
        ),
        listOf(
            TimeInterval(
                TimeOfDay(hour = 2, minute = 0, isAM = false),
                TimeOfDay(hour = 11, minute = 0, isAM = false),
            )
        ),
        listOf(
            TimeInterval(
                TimeOfDay(hour = 2, minute = 0, isAM = false),
                TimeOfDay(hour = 11, minute = 0, isAM = false),
            )
        ),
        listOf(
            TimeInterval(
                TimeOfDay(hour = 2, minute = 0, isAM = true),
                TimeOfDay(hour = 4, minute = 0, isAM = true),
            ),
            TimeInterval(
                TimeOfDay(hour = 6, minute = 0, isAM = false),
                TimeOfDay(hour = 11, minute = 0, isAM = false),
            )
        ),
        listOf(
            TimeInterval(
                TimeOfDay(hour = 12, minute = 0, isAM = false),
                TimeOfDay(hour = 10, minute = 0, isAM = false),
            )
        ),
    ),
    popularTimes = PopularTimes(
        startTime = TimeOfDay(8, 0, true),
        busyList = listOf(50, 30, 3, 60, 90, 0, 20, 11, 6, 93, 76, 0, 8, 9, 100)
    ),
    equipmentGroupings = listOf(
        EquipmentGrouping(
            "Cardio Machines",
            listOf(
                Pair("Precor treadmills", 10),
                Pair("Elliptical trainers", 12),
                Pair("AMTs", 4),
                Pair("Expresso bikes", 5),
                Pair("Recumbent and upright bikes", 3),
            )
        ),
        EquipmentGrouping(
            "Precor Selectorized Machines",
            listOf(
                Pair("Leg Press", 10),
                Pair("Seated Calf Raises", 12),
                Pair("Seated Leg Curl", 4),
                Pair("Leg Extensions", 5),
                Pair("Inner/Outer ttwtf is this", 3),
                Pair("Glute Extensions", 3),
                Pair("Converging Sus Machine", 3),
                Pair("Converging Chin Definer", 3),
                Pair("Dip/Chin Assistance", 3),
                Pair("Rear Deltoid Pulldowns", 3),
            )
        )
    ),
    gymnasiumInfo = listOf(
        GymnasiumInfo(
            hours = listOf(
                TimeInterval(start = TimeOfDay(6), end = TimeOfDay(7)),
                TimeInterval(start = TimeOfDay(7, 30), end = TimeOfDay(10)),
            ),
            courts = listOf(
                CourtInfo(
                    "Basketball", hours = listOf(
                        TimeInterval(start = TimeOfDay(6), end = TimeOfDay(7)),
                        TimeInterval(start = TimeOfDay(7, 30), end = TimeOfDay(10)),
                    )
                ),
                CourtInfo(
                    "Volleyball", hours = listOf(
                        TimeInterval(start = TimeOfDay(7, 30), end = TimeOfDay(10)),
                    )
                )
            )
        ),
        null,
        null,
        null,
        null,
        null,
        null,

        ),
    swimmingInfo = listOf(
        SwimmingInfo(swimmingTimes = exampleSwimmingList1),
        null,
        SwimmingInfo(swimmingTimes = exampleSwimmingList1),
        SwimmingInfo(swimmingTimes = exampleSwimmingList2),
        SwimmingInfo(swimmingTimes = exampleSwimmingList1),
        SwimmingInfo(swimmingTimes = exampleSwimmingList2),
        SwimmingInfo(swimmingTimes = exampleSwimmingList1),
    ),
    bowlingInfo = exampleBowlingList,
    miscellaneous = listOf("Game area", "Outdoor basketball court", "Bouldering wall"),
    classesToday = listOf(
        exampleClassMusclePump,
        exampleClassMusclePump
    ),
    imageUrl = "https://recreation.athletics.cornell.edu/sites/recreation.athletics.cornell.edu/files/photo-galleries/DB%20_%20Benches_TM.jpeg"

)