package com.cornellappdev.uplift.ui.screens.profile

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.components.general.UpliftTopBarWithBack
import com.cornellappdev.uplift.ui.viewmodels.profile.SettingsViewModel
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.GRAY03
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.montserratFamily

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = hiltViewModel(),
) {
    val settingsUiState = settingsViewModel.collectUiStateValue()
    SettingsScreenContent(
        isLoggedIn = settingsUiState.isLoggedIn,
        onBackClick = settingsViewModel::onBack,
        onAboutPressed = settingsViewModel::onAboutPressed,
        onRemindersPressed = settingsViewModel::onRemindersPressed,
        onReportPressed = settingsViewModel::onReportPressed,
        onLogOut = settingsViewModel::onLogOut
    )
}

@Composable
private fun SettingsScreenContent(
    isLoggedIn: Boolean,
    onBackClick: () -> Unit,
    onAboutPressed: () -> Unit,
    onRemindersPressed: () -> Unit,
    onReportPressed: () -> Unit,
    onLogOut: () -> Unit
) {
    Scaffold(topBar = {
        UpliftTopBarWithBack(title = "Settings", onBackClick = onBackClick)
    }) { padding ->
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
            SettingsOption(
                icon = R.drawable.ic_appdev,
                title = "About Uplift",
                onClick = onAboutPressed,
            )
            Divider(color = GRAY01)
            SettingsOption(
                icon = R.drawable.ic_reminders_clock,
                title = "Reminders",
                onClick = onRemindersPressed,
            )
            Divider(color = GRAY01)
            SettingsOption(
                icon = R.drawable.ic_report,
                title = "Report an Issue",
                onClick = onReportPressed,
            )
            Divider(color = GRAY01)
            if (isLoggedIn) {
                LogOutButton(onLogOut)
            }
        }
    }
}

@Composable
private fun LogOutButton(
    onLogOut: () -> Unit,
){
    Row(
        modifier = Modifier.clickable(
            onClick = onLogOut
        ),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ){
        Icon(
            painterResource(id = R.drawable.ic_logout),
            contentDescription = "Log out",
            tint = Color.Unspecified
        )
        Text(
            text = "Log out",
            fontSize = 16.sp,
            fontFamily = montserratFamily,
            fontWeight = FontWeight.Medium,
            color = Color(0xFFFE1313)
        )
    }
}

@Composable
private fun SettingsOption(
    @DrawableRes icon: Int,
    title: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(painterResource(id = icon), contentDescription = title, tint = Color.Unspecified)
            Text(
                text = title,
                fontSize = 16.sp,
                fontFamily = montserratFamily,
                fontWeight = FontWeight.Medium,
                color = PRIMARY_BLACK
            )
        }
        Icon(
            painterResource(id = R.drawable.chevron_right),
            contentDescription = null,
            tint = GRAY03
        )
    }
}


@Preview
@Composable
private fun SettingsScreenPreview() {
    SettingsScreenContent(
        isLoggedIn = true,
        onBackClick = {},
        onAboutPressed = {},
        onRemindersPressed = {},
        onReportPressed = {},
        onLogOut = {}
    )
}