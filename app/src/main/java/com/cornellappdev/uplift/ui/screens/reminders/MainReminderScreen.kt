package com.cornellappdev.uplift.ui.screens.reminders

import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.components.general.UpliftTopBarWithBack
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.montserratFamily
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainReminderScreen(
    onBack: () -> Unit
) {
    val systemUiController: SystemUiController = rememberSystemUiController()

    systemUiController.isStatusBarVisible = false
    systemUiController.isNavigationBarVisible = false // Navigation bar
    systemUiController.isSystemBarsVisible = false

    Scaffold(topBar = {
        UpliftTopBarWithBack(
            title = "Reminders",
            onBackClick = onBack,
            withBack = true
        )
    }) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                ReminderListItem("Class Reminders", R.drawable.ic_classes_inactive, {})
                HorizontalDivider(color = GRAY01)
                ReminderListItem("Workout Reminders", R.drawable.gym_simple, {})
                HorizontalDivider(color = GRAY01)
                ReminderListItem("Capacity Reminders", R.drawable.capacities, {})
            }
        }
    }
}

@Composable
private fun ReminderListItem(txt: String, resourceId: Int, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .height(72.dp)
            .fillMaxWidth()
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(resourceId), contentDescription = "")

            Text(
                txt,
                fontSize = 16.sp,
                fontFamily = montserratFamily,
                fontWeight = FontWeight.Normal
            )
        }

        IconButton(onClick) {
            Icon(painter = painterResource(R.drawable.chevron_right), contentDescription = "")
        }
    }
}

@Preview
@Composable
private fun MainReminderScreenPreview() {
    MainReminderScreen(
        onBack = {}
    )
}