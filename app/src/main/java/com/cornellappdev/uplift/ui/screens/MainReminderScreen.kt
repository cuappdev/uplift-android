package com.cornellappdev.uplift.ui.screens

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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.LIGHT_GRAY
import com.cornellappdev.uplift.util.montserratFamily
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainReminderScreen() {
    val systemUiController: SystemUiController = rememberSystemUiController()

    systemUiController.isStatusBarVisible = false
    systemUiController.isNavigationBarVisible = false // Navigation bar
    systemUiController.isSystemBarsVisible = false

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            expandedHeight = 40.dp,
            title = {
                Text(
                    text = "Reminders",
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontFamily = montserratFamily,
                    fontWeight = FontWeight.Bold
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = LIGHT_GRAY),
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(painter = painterResource(R.drawable.backarrow), contentDescription = "")
                }
            }

        )
    }) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
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