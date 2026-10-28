package com.cornellappdev.uplift.ui.screens.reminders

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cornellappdev.uplift.ui.components.general.ReminderSwitch

@Composable
fun ClassReminderScreen() {
    ClassRemindersContent()
}

@Composable
fun ClassRemindersContent() {

    ReminderSwitch(
        checked = false,
        onCheckedChange = {}
    )
}

@Preview
@Composable
fun ClassRemindersContentPreview() {
    ClassReminderScreen()
}