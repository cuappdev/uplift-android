package com.cornellappdev.uplift.ui.screens.reminders

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cornellappdev.uplift.ui.components.reminders.DescriptionReminderSwitch
import com.cornellappdev.uplift.ui.components.general.UpliftTopBarWithBack
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
        }
    }

}

@Preview
@Composable
fun ClassRemindersContentPreview() {
    ClassReminderContent(
        onBack = {},
        switchChecked = true,
        setToggle = {}
    )
}