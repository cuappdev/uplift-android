package com.cornellappdev.uplift.ui.components.capacityreminder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.ui.components.general.CalendarBar
import com.cornellappdev.uplift.ui.components.general.DayOfWeekBar
import com.cornellappdev.uplift.util.GRAY03
import com.cornellappdev.uplift.util.montserratFamily

/**
 * Section with days to remind the user to check the capacity.
 */
@Composable
fun ReminderDays(
    initialDays: Set<String> = emptySet(),
    onDaySelected: (Set<String>) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "REMINDER DAYS",
            fontFamily = montserratFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = GRAY03
        )
        DayOfWeekBar(
            initialSelectedDays = initialDays,
            onDaySelected = onDaySelected
        )

    }
}