package com.cornellappdev.uplift.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.data.models.gymdetail.UpliftGym
import com.cornellappdev.uplift.util.ACCENT_CLOSED
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.montserratFamily

/**
 * Displays bowling information for the given gym throughout the week, defaulting to the day
 * given by parameter [today].
 */
@Composable
fun GymBowlingSection(today: Int, gym: UpliftGym) {
    var selectedDay by remember { mutableStateOf(today) }
    val bowlingInfo = gym.bowlingInfo?.get(selectedDay)

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(5.dp))
        DayOfWeekSelector(today = today) { day ->
            selectedDay = day
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (bowlingInfo != null) {
            for (interval in bowlingInfo.hours) {
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
                Spacer(Modifier.padding(bottom = 6.dp))
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(Modifier.weight(1f))
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = GRAY01,
                    modifier = Modifier
                        .weight(2f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Price per game",
                                fontFamily = montserratFamily,
                                fontSize = 14.sp,
                                fontWeight = FontWeight(400),
                                lineHeight = 20.sp,
                                textAlign = TextAlign.Left,
                                modifier = Modifier.padding(bottom = 11.dp),
                                color = PRIMARY_BLACK
                            )
                            Text(
                                text = bowlingInfo.pricePerGame,
                                fontFamily = montserratFamily,
                                fontSize = 14.sp,
                                fontWeight = FontWeight(600),
                                lineHeight = 20.sp,
                                textAlign = TextAlign.Left,
                                modifier = Modifier.padding(bottom = 11.dp),
                                color = PRIMARY_BLACK
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Shoe rental",
                                fontFamily = montserratFamily,
                                fontSize = 14.sp,
                                fontWeight = FontWeight(400),
                                lineHeight = 20.sp,
                                textAlign = TextAlign.Left,
                                color = PRIMARY_BLACK
                            )
                            Text(
                                text = bowlingInfo.shoeRental,
                                fontFamily = montserratFamily,
                                fontSize = 14.sp,
                                fontWeight = FontWeight(600),
                                lineHeight = 20.sp,
                                textAlign = TextAlign.Left,
                                color = PRIMARY_BLACK
                            )
                        }
                    }
                }
                Spacer(Modifier.weight(1f))
            }
            Spacer(Modifier.height(12.dp))
        } else {
            Text(
                text = "Closed",
                fontFamily = montserratFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight(500),
                lineHeight = 26.sp,
                textAlign = TextAlign.Left,
                color = ACCENT_CLOSED,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }
    }
}
