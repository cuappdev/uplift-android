package com.cornellappdev.uplift.ui.components.goalsetting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.screens.Reminder
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.montserratFamily

/**
 * EditReminderSection is a composable that allows the user to edit a reminder.
 * @param reminder the reminder to edit
 * @param addNewReminderState whether the reminder is new
 * @param onReminderSave callback for when the reminder is saved
 * @param openDelete callback for when the delete button is clicked
 * @sample EditReminderSection(
 *    reminder = Reminder(time = "9:00 AM", days = setOf("W"), enabled = true),
 *    addNewReminderState = false,
 *    onReminderSave = { reminder -> println(reminder) },
 *    openDelete = { println("Delete") }
 *    )
 */
@Composable
fun EditReminderSection(
    reminder: Reminder?,
    addNewReminderState: Boolean,
    onReminderSave: (Reminder) -> Unit,
    openDelete: () -> Unit
) {
    val reminderTimeParams = reminder?.time?.split(":")
    val reminderTimeMinMeridian = reminderTimeParams?.get(1)?.split(" ")
    var selectedDays by remember { mutableStateOf(reminder?.days ?: setOf("W")) }
    var checkedState by remember {
        mutableStateOf(reminder != null && reminder.days.size == 7)
    }
    var setTimeOn by remember {
        mutableStateOf(reminder != null && reminder.time != "9:00 AM")
    }
    var hour by remember { mutableIntStateOf(reminderTimeParams?.get(0)?.toInt() ?: 9) }
    var minute by remember { mutableStateOf(reminderTimeMinMeridian?.get(0) ?: "00") }
    var isAm by remember {
        mutableStateOf(if (reminder != null) reminderTimeMinMeridian?.get(1) == "AM" else true)
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        EditReminderCard(
            reminder = reminder,
            selectedDays = selectedDays,
            onDaySelected = { selectedDays = it },
            checkedState = checkedState,
            onCheckedChange = { checkedState = it },
            setTimeOn = setTimeOn,
            onSetTimeChange = {
                setTimeOn = it
                if (!it) {
                    hour = 9
                    minute = "00"
                    isAm = true
                }
            },
            hour = hour,
            minute = minute,
            isAm = isAm,
            onHourChange = { hour = it },
            onMinuteChange = { minute = it },
            onAmChange = { isAm = it },
            addNewReminderState = addNewReminderState
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(
                8.dp, Alignment.CenterHorizontally
            )
        ) {
            DeleteButton(onClick = openDelete)
            SubmitReminderButton(onClick = {
                onReminderSave(
                    Reminder(
                        time = "$hour:$minute ${if (isAm) "AM" else "PM"}",
                        days = selectedDays,
                        enabled = true
                    )
                )
            })

        }
    }
}

/**
 * Button that deletes the reminder
 * @param onClick callback for when the button is clicked
 * @sample DeleteButton(onClick = { println("Delete") })
 */
@Composable
private fun DeleteButton(
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(38.dp),
        modifier = Modifier
            .width(120.dp)
            .height(40.dp),
        color = Color.White,
        shadowElevation = 6.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_trash),
                contentDescription = "Delete",
            )
            Text(
                text = "Delete",
                fontFamily = montserratFamily,
                fontSize = 14.sp,
                color = PRIMARY_BLACK,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 5.dp)
            )
        }
    }
}

/**
 * Button that submits the reminder
 * @param onClick callback for when the button is clicked
 * @sample SubmitReminderButton(onClick = { println("Submit") })
 */
@Composable
private fun SubmitReminderButton(
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(38.dp),
        modifier = Modifier
            .width(120.dp)
            .height(40.dp),
        color = PRIMARY_BLACK,
        shadowElevation = 6.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_check),
                contentDescription = "Done",
                tint = Color.White
            )
            Text(
                text = "Done",
                fontFamily = montserratFamily,
                fontSize = 14.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 5.dp)
            )
        }
    }
}

