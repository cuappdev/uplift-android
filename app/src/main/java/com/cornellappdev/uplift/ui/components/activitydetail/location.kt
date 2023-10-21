package com.cornellappdev.uplift.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.models.OpenType
import com.cornellappdev.uplift.models.UpliftActivity
import com.cornellappdev.uplift.models.UpliftClass
import com.cornellappdev.uplift.ui.screens.LineSpacer
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.isCurrentlyOpen
import com.cornellappdev.uplift.util.montserratFamily
import com.cornellappdev.uplift.util.sampleLocation_A
import com.cornellappdev.uplift.util.sampleLocation_B
import java.util.Calendar

/**
 * A component displaying brief information as to a [UpliftClass]. Used on the home screen.
 *
 * @param openIds A set containing all the location gymIds that this section should have open.
 * @param onTabSelect A function that fires when a tab is pressed.
 */
@Composable
fun LocationSection(
    locations: List<UpliftActivity>,
    today: Int,
    openIds: Set<String>,
    onTabSelect: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "LOCATIONS",
            fontFamily = montserratFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight(700),
            lineHeight = 19.5.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = PRIMARY_BLACK
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Display each location as a LocationTab
        for ((i, location) in locations.withIndex()) {
            FacilityTab(
                painter = null,
                location.name,
                collapsed = !openIds.contains(location.gymId),
                onClick = {
                    onTabSelect(location.gymId)
                },
                open = when (location.hours[today] != null
                        && isCurrentlyOpen(location.hours[today])) {
                    true -> OpenType.OPEN
                    else -> OpenType.CLOSED
                }

            ) {
                ActivityInfoSection(today = today, location = location)

            }


            LineSpacer(paddingStart = 24.dp, paddingEnd = 24.dp)
        }
    }
}

@Preview
@Composable
fun LocationSectionPreview() {
    val mockLocations = listOf(
        sampleLocation_A,
        sampleLocation_B
    )
    val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1
    val gymIdsSet: MutableState<Set<String>> = remember { mutableStateOf(setOf()) }

    LocationSection(
        locations = mockLocations, today = today, openIds = gymIdsSet.value,
    ) { location ->
        val set = gymIdsSet.value.toMutableSet()
        if (gymIdsSet.value.contains(location)) {
            set.remove(location)
        } else {
            set.add(location)
        }
        gymIdsSet.value = set
    }
}
