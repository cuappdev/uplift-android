package com.cornellappdev.uplift.ui.screens.profile

import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.components.profile.ProfileErrorDialog
import com.cornellappdev.uplift.ui.components.general.UpliftTopBarWithBack
import com.cornellappdev.uplift.ui.components.profile.DeleteAccountConfirmationDialog
import com.cornellappdev.uplift.ui.theme.UpliftTheme
import com.cornellappdev.uplift.ui.theme.AppColors
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
    var showDeleteDialog by remember { mutableStateOf(false)}
    BackHandler(enabled = settingsUiState.isDeleting) { }
    SettingsScreenContent(
        isLoggedIn = settingsUiState.isLoggedIn,
        isDeleting = settingsUiState.isDeleting,
        onBackClick = settingsViewModel::onBack,
        onAboutPressed = settingsViewModel::onAboutPressed,
        onReportPressed = settingsViewModel::onReportPressed,
        onLogOut = settingsViewModel::onLogOut,
        onDeleteClick = {
            if (!settingsUiState.isDeleting) showDeleteDialog = true
        },
    )
    if (settingsUiState.showDeleteError) {
        ProfileErrorDialog(
            title = stringResource(R.string.profile_delete_error_title),
            message = stringResource(R.string.profile_delete_error_message),
            onDismiss = {
                showDeleteDialog = false
                settingsViewModel.dismissError()
            },
            onRetry = {
                showDeleteDialog = false
                settingsViewModel.onDeleteAccount()
            }
        )
    } else if (showDeleteDialog && !settingsUiState.isDeleting) {
        DeleteAccountConfirmationDialog(
            onDismiss = {
                showDeleteDialog = false
            },
            onConfirm = {
                showDeleteDialog = false
                settingsViewModel.onDeleteAccount()
            }
        )
    }
}

@Composable
private fun SettingsScreenContent(
    isLoggedIn: Boolean,
    onBackClick: () -> Unit,
    onAboutPressed: () -> Unit,
    onReportPressed: () -> Unit,
    onLogOut: () -> Unit,
    onDeleteClick: () -> Unit,
    isDeleting: Boolean = false,
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
                enabled = !isDeleting,
            )
            Divider(color = GRAY01)
            SettingsOption(
                icon = R.drawable.ic_report,
                title = "Report an Issue",
                onClick = onReportPressed,
                enabled = !isDeleting,
            )
            Divider(color = GRAY01)
            if (isLoggedIn) {
                LogOutButton(onLogOut, enabled = !isDeleting)
                Divider(color = GRAY01)
                DeleteAccountButton(onDeleteClick, isDeleting = isDeleting)
            }
        }
    }
}

@Composable
private fun LogOutButton(
    onLogOut: () -> Unit,
    enabled: Boolean = true,
){
    Row(
        modifier = Modifier.alpha(if (enabled) 1f else 0.5f).clickable(
            onClick = onLogOut,
            enabled = enabled
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
            color = AppColors.Red
        )
    }
}

@Composable
private fun DeleteAccountButton(
    onDeleteClick: () -> Unit,
    isDeleting: Boolean = false,
){
    Row(
        modifier = Modifier.alpha(if (isDeleting) 0.5f else 1f).clickable(
            onClick = onDeleteClick,
            enabled = !isDeleting
        ),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ){
        Icon(
            painterResource(id = R.drawable.ic_trash),
            contentDescription = "Delete account",
            tint = AppColors.Red
        )
        Text(
            text = if (isDeleting) stringResource(R.string.deleting_account) else "Delete account",
            fontSize = 16.sp,
            fontFamily = montserratFamily,
            fontWeight = FontWeight.Medium,
            color = AppColors.Red
        )
    }
}

@Composable
private fun SettingsOption(
    @DrawableRes icon: Int,
    title: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (enabled) 1f else 0.5f)
            .clickable(
                onClick = onClick,
                enabled = enabled,
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
        onReportPressed = {},
        onLogOut = {},
        onDeleteClick = {},
    )
}

@Preview(name = "Deleting account", showBackground = true)
@Composable
private fun SettingsDeletingPreview() {
    UpliftTheme {
        SettingsScreenContent(
            isLoggedIn = true,
            isDeleting = true,
            onBackClick = {},
            onAboutPressed = {},
            onReportPressed = {},
            onLogOut = {},
            onDeleteClick = {},
        )
    }
}
