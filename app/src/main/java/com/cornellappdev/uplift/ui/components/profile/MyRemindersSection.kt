package com.cornellappdev.uplift.ui.components.profile

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.ui.components.general.ReminderSwitch
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.PRIMARY_YELLOW
import com.cornellappdev.uplift.util.montserratFamily

data class ReminderItem(
    val dayOfWeek: String,
    val startTime: String,
    val endTime: String,
    val toggledOn: Boolean,
    val onToggle: (Boolean) -> Unit = {},
)

@Composable
fun MyRemindersSection(
    reminderItems: List<ReminderItem>,
    onClickHeader: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SectionTitleText("My Reminders", onClickHeader)
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(reminderItems) { reminderItem ->
                ReminderItemCard(
                    dayOfWeek = reminderItem.dayOfWeek,
                    startTime = reminderItem.startTime,
                    endTime = reminderItem.endTime,
                    toggledOn = reminderItem.toggledOn,
                    onToggle = reminderItem.onToggle
                )
            }
        }
    }
}

@Composable
private fun ReminderItemCard(
    dayOfWeek: String,
    startTime: String,
    endTime: String,
    toggledOn: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .width(170.dp)
            .border(
                width = 1.dp,
                color = if (toggledOn) PRIMARY_YELLOW else GRAY01,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(
                vertical = 16.dp,
                horizontal = 8.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = dayOfWeek,
            fontFamily = montserratFamily,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = startTime,
                fontFamily = montserratFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = endTime,
                fontFamily = montserratFamily,
                fontSize = 14.sp,
            )
        }
        ReminderSwitch(
            checked = toggledOn,
            onCheckedChange = { onToggle(toggledOn) }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MyRemindersSectionPreview() {
    val reminderItems = listOf(
        ReminderItem("Mon", "8:00 AM", "9:00 AM", true),
        ReminderItem("Tue", "8:00 AM", "12:00 PM", false),
        ReminderItem("Wed", "8:00 AM", "9:00 AM", true),
        ReminderItem("Thu", "8:00 AM", "9:00 AM", false),
        ReminderItem("Fri", "11:30 AM", "12:00 PM", true),
    )
    MyRemindersSection(
        reminderItems = reminderItems,
        onClickHeader = {}
    )
}