package com.cornellappdev.uplift.ui.screens.reminders

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cornellappdev.uplift.ui.components.general.RadioCheckButton
import com.cornellappdev.uplift.ui.components.reminders.DescriptionReminderSwitch
import com.cornellappdev.uplift.ui.components.general.UpliftTopBarWithBack
import com.cornellappdev.uplift.ui.components.reminders.classreminder.ClassReminderInput
import com.cornellappdev.uplift.ui.viewmodels.reminders.ClassReminderViewModel
import com.cornellappdev.uplift.util.GRAY03
import com.cornellappdev.uplift.util.montserratFamily

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ClassReminderScreen(
    classReminderViewModel: ClassReminderViewModel = hiltViewModel()
) {
    val classRemindersUiState = classReminderViewModel.collectUiStateValue()
    val checked = classRemindersUiState.toggledOn

    ClassReminderContent(
        onBack = classReminderViewModel::onBack,
        switchChecked = checked,
        setToggle = classReminderViewModel::setToggle
    )

}

@Composable
private fun ClassReminderContent(
    onBack: () -> Unit,
    switchChecked: Boolean,
    setToggle: (Boolean) -> Unit
) {
    Scaffold(topBar = {
        UpliftTopBarWithBack(
            title = "Class Reminders",
            onBackClick = onBack,
            withBack = true
        )
    }) {  padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(
                        top = padding.calculateTopPadding() + 24.dp,
                        start = 24.dp,
                        end = 24.dp,
                    ),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    DescriptionReminderSwitch(
                        text = "CLASS REMINDERS",
                        checked = switchChecked,
                        onCheckedChange = setToggle
                    )
                    Text(
                        text = "Uplift will send a notification before your favorite classes.",
                        fontFamily = montserratFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = GRAY03
                    )
                }

                AnimatedVisibility(
                    visible = switchChecked, enter = fadeIn(), exit = fadeOut()
                ) {
                    ClassRemindersSettings()
                }
                Spacer(modifier = Modifier.height(32.dp))

            }
    }

}

@Composable
private fun ClassRemindersSettings() {
    var checkedButton by remember { mutableIntStateOf(1) }
    var userInputHours by remember { mutableStateOf("") }
    var userInputMinutes by remember { mutableStateOf("") }
    var isMinutesError = userInputMinutes.toIntOrNull() != null && userInputMinutes.toInt() > 59

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "TIME BEFORE CLASS",
                fontFamily = montserratFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = GRAY03
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                RadioCheckButton(
                    "30 min",
                    selected = checkedButton == 1,
                    onButtonSelected = { checkedButton = 1 }
                )

                RadioCheckButton(
                    "60 min",
                    selected = checkedButton == 2,
                    onButtonSelected = { checkedButton = 2 }
                )
            }

            RadioCheckButton(
                "Custom",
                selected = checkedButton == 3,
                onButtonSelected = { checkedButton = 3 }
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ClassReminderInput(
                        label = "hours",
                        placeholder = "0",
                        enabled = checkedButton == 3,
                        textValue = userInputHours,
                        onValueChange = { input ->
                            if (input.all { it.isDigit() } && input.length <= 2) {
                                userInputHours = input
                            }
                        },
                        errorState = false,
                        onErrorStateChange = {}
                    )

                    ClassReminderInput(
                        label = "minutes",
                        placeholder = "0",
                        enabled = checkedButton == 3,
                        textValue = userInputMinutes,
                        onValueChange = { input ->
                            if (input.all { it.isDigit() } && input.length <= 2) {
                                userInputMinutes = input
                            }
                        },
                        errorState = isMinutesError,
                        onErrorStateChange = {}
                    )
                }

                if (isMinutesError && checkedButton == 3) {
                    Text(
                        text = "Invalid minutes input",
                        color = Color.Red,
                        fontFamily = montserratFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

        }

    }
}



@Preview
@Composable
fun ClassRemindersContentPreview() {
    var checked by remember { mutableStateOf(true) }
    ClassReminderContent(
        onBack = {},
        switchChecked = checked,
        setToggle = { checked = it }
    )
}