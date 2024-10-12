package com.cornellappdev.uplift.ui.screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.ui.components.capacityreminder.CapacityReminderSwitch
import com.cornellappdev.uplift.ui.components.capacityreminder.CapacityThreshold
import com.cornellappdev.uplift.ui.components.general.UpliftTopBarWithBack
import com.cornellappdev.uplift.util.GRAY03
import com.cornellappdev.uplift.util.montserratFamily
import com.cornellappdev.uplift.ui.components.capacityreminder.LocationsToRemind
import com.cornellappdev.uplift.ui.components.capacityreminder.ReminderDays

/**
 * Screen for the capacity reminder feature.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CapacityReminderScreen(
    checked: Boolean,
    onChecked: (Boolean) -> Unit = {},
    initialSelectedDays: Set<String> = emptySet(),
    onDaySelected: (Set<String>) -> Unit = {},
    capacityThreshold: Float = 0.5f,
    onSliderChange: (Float) -> Unit = {},
    initialSelectedGyms: Set<String> = emptySet(),
    onGymSelected: (Set<String>) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    var switchChecked by remember { mutableStateOf(checked) }
    Scaffold(topBar = {
        UpliftTopBarWithBack(title = "Capacity Reminder") {
            onBackClick()
        }
    }) { padding ->
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(
                    top = padding.calculateTopPadding() + 24.dp, start = 16.dp, end = 16.dp
                ), verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CapacityReminderSwitch(
                    checked = switchChecked,
                    onCheckedChange = {
                        switchChecked = it
                        onChecked(it)
                    })
                Text(
                    text = "Uplift will send you a notification when gyms dip below the set capacity",
                    fontFamily = montserratFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = GRAY03
                )
            }
            AnimatedVisibility(
                visible = switchChecked, enter = fadeIn(), exit = fadeOut()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ReminderDays(initialSelectedDays, onDaySelected)
                    CapacityThreshold(capacityThreshold, onSliderChange)
                    LocationsToRemind(initialSelectedGyms, onGymSelected)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun CapacityReminderScreenPreview() {
    val checked by remember { mutableStateOf(true) }
    CapacityReminderScreen(
        checked,
        {},
        setOf("M", "Tu", "W", "Th", "F"),
        {},
        0.75f,
        {},
        setOf("Teagle Up", "Teagle Down", "Helen Newman"),
        {}) {}
}
