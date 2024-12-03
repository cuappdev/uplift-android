package com.cornellappdev.uplift.ui.components.gymdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.fragment.GymFields
import com.cornellappdev.uplift.models.PopularTimes
import com.cornellappdev.uplift.models.TimeOfDay
import com.cornellappdev.uplift.models.UpliftGym
import com.cornellappdev.uplift.ui.components.GymHours
import com.cornellappdev.uplift.ui.components.PopularTimesSection
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.isOpen
import com.cornellappdev.uplift.util.montserratFamily
import com.cornellappdev.uplift.util.todayIndex

@Composable
fun FitnessCenterContent(
    gym: UpliftGym?
) {
    val day = todayIndex()
    Column(
        modifier = Modifier.fillMaxSize()
            .background(color = Color.White)
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        GymHours(
            gym!!.hours, day, isOpen(gym.hours[day])
        )
        SectionDivider()
        PopularTimesSection(
            popularTimes = PopularTimes(
                busyList = listOf(20, 30, 40, 50, 50, 45, 35, 40, 50, 70, 80, 90, 95, 85, 70, 65),
                startTime = TimeOfDay(
                    hour = 6,
                    minute = 0,
                    isAM = true
                )
            )
        )
        SectionDivider()
        GymAmenitySection(gym.amenities)
        SectionDivider()
        GymEquipmentSection()
    }

}

@Composable
private fun SectionDivider() {
    HorizontalDivider(
        color = GRAY01,
        thickness = 1.dp,
    )
}

