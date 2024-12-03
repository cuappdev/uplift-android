package com.cornellappdev.uplift.ui.components.capacityreminder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.ui.components.general.ReminderSwitch
import com.cornellappdev.uplift.util.GRAY04
import com.cornellappdev.uplift.util.PRIMARY_YELLOW
import com.cornellappdev.uplift.util.montserratFamily

/**
 * A switch that toggles capacity reminders on and off.
 */
@Composable
fun CapacityReminderSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp),

        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "CAPACITY REMINDERS",
            fontFamily = montserratFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = GRAY04
        )
        ReminderSwitch(checked = checked, onCheckedChange = onCheckedChange)
    }
}