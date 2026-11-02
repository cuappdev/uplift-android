package com.cornellappdev.uplift.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.components.general.UpliftButton
import com.cornellappdev.uplift.ui.components.general.UpliftTopBarWithBack
import com.cornellappdev.uplift.ui.components.goalsetting.GoalSettingHeader
import com.cornellappdev.uplift.ui.components.goalsetting.GoalSlider
import com.cornellappdev.uplift.ui.theme.UpliftTheme
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.GRAY04
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.bebasNeueFamily
import com.cornellappdev.uplift.util.montserratFamily

@Composable
fun MyGoalsScreen(
    initialGoalValue: Int,
    onBackClick: () -> Unit,
    onSaveGoal: (Int) -> Unit,
    isInitiallyLocked: Boolean = false,
    lockedUntilDate: String = "5/12/26",
    lockedDaysRemaining: Int = 30
) {
    var currentGoal by remember(initialGoalValue) { mutableIntStateOf(initialGoalValue) }
    val hasChanged = currentGoal != initialGoalValue

    var isSaved by remember { mutableStateOf(value = false) }
    var showConfirmationDialog by remember { mutableStateOf(value = false) }
    var showLockedDialog by remember { mutableStateOf(value = isInitiallyLocked) }

    Scaffold(
        topBar = {
            UpliftTopBarWithBack(
                title = stringResource(R.string.goals_title),
                onBackClick = onBackClick,
                withBack = true
            )
        },
        containerColor = Color.White,
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.padding(vertical = 24.dp, horizontal = 18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                GoalSettingHeader(isOnboarding = false)
                GoalSlider(
                    value = currentGoal,
                    onValueChange = {
                        if (isInitiallyLocked) {
                            showLockedDialog = true
                        } else {
                            currentGoal = it
                            isSaved = false
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            SaveGoalButton(
                isSaved = isSaved,
                enabled = hasChanged && !isInitiallyLocked,
                onClick = { showConfirmationDialog = true }
            )
        }

        if (showConfirmationDialog) {
            SaveGoalConfirmationDialog(
                onDismiss = { showConfirmationDialog = false },
                onConfirm = {
                    showConfirmationDialog = false
                    isSaved = true
                    onSaveGoal(currentGoal)
                }
            )
        }

        if (showLockedDialog) {
            GoalLockedDialog(
                lockedUntilDate = lockedUntilDate,
                lockedDaysRemaining = lockedDaysRemaining,
                onDismiss = { showLockedDialog = false }
            )
        }
    }
}

/**
 * The Save Changes button, which switches to a disabled Saved state once the goal is saved.
 */
@Composable
private fun SaveGoalButton(
    isSaved: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    if (isSaved) {
        UpliftButton(
            onClick = {},
            enabled = false,
            text = stringResource(R.string.goals_saved),
            width = 165.dp,
            height = 41.dp,
            fontSize = 16f,
            containerColor = GRAY01,
            contentColor = PRIMARY_BLACK,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_check),
                    contentDescription = null,
                    tint = PRIMARY_BLACK,
                    modifier = Modifier.padding(end = 8.dp).size(18.dp)
                )
            }
        )
    } else {
        UpliftButton(
            onClick = onClick,
            enabled = enabled,
            text = stringResource(R.string.goals_save_changes),
            width = 165.dp,
            height = 41.dp,
            fontSize = 16f,
            containerColor = PRIMARY_BLACK,
            contentColor = Color.White
        )
    }
}

/**
 * Asks the user to confirm saving their goal, since it will be locked for one month afterwards.
 */
@Composable
private fun SaveGoalConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    GoalsAlertDialog(onDismiss = onDismiss, iconSize = 36.dp) {
        Text(
            text = stringResource(R.string.goals_save_warning),
            fontFamily = montserratFamily,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = PRIMARY_BLACK,
            textAlign = TextAlign.Center
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            UpliftButton(
                onClick = onConfirm,
                text = stringResource(R.string.goals_continue),
                width = 160.dp,
                height = 41.dp,
                fontSize = 14f,
                containerColor = PRIMARY_BLACK,
                contentColor = Color.White
            )
            Text(
                text = stringResource(R.string.goals_back),
                fontFamily = montserratFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = GRAY04,
                modifier = Modifier
                    .clickable(onClick = onDismiss)
                    .padding(vertical = 8.dp)
            )
        }
    }
}

/**
 * Tells the user their goal is locked and how long until they can change it.
 */
@Composable
private fun GoalLockedDialog(
    lockedUntilDate: String,
    lockedDaysRemaining: Int,
    onDismiss: () -> Unit
) {
    GoalsAlertDialog(onDismiss = onDismiss, iconSize = 33.dp) {
        Text(
            text = stringResource(R.string.goals_locked_message, lockedUntilDate),
            fontFamily = montserratFamily,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = PRIMARY_BLACK,
            textAlign = TextAlign.Center
        )
        HorizontalDivider(color = GRAY01, thickness = 1.dp)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(R.string.goals_change_in),
                fontFamily = montserratFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = GRAY04
            )
            Text(
                text = pluralStringResource(
                    R.plurals.goals_days_remaining,
                    lockedDaysRemaining,
                    lockedDaysRemaining
                ),
                fontFamily = bebasNeueFamily,
                fontSize = 32.sp,
                color = PRIMARY_BLACK
            )
        }
    }
}

/**
 * Shared layout for the Goals dialogs: a white rounded dialog with a close button in the top
 * right and a clock icon above [content].
 */
@Composable
private fun GoalsAlertDialog(
    onDismiss: () -> Unit,
    iconSize: Dp,
    content: @Composable ColumnScope.() -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        dismissButton = {},
        containerColor = Color.White,
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.width(280.dp),
        text = {
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(32.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = stringResource(R.string.close),
                        tint = PRIMARY_BLACK
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_reminders_clock),
                        contentDescription = null,
                        tint = PRIMARY_BLACK,
                        modifier = Modifier.size(iconSize)
                    )
                    content()
                }
            }
        }
    )
}

@Preview(showBackground = true, name = "Initial State")
@Composable
private fun MyGoalsScreenInitialPreview() {
    UpliftTheme{
        MyGoalsScreen(
            initialGoalValue = 3,
            onBackClick = {},
            onSaveGoal = {}
        )
    }
}

@Preview(showBackground = true, name = "Locked State Dialog")
@Composable
private fun MyGoalsScreenLockedPreview() {
    UpliftTheme{
        MyGoalsScreen(
            initialGoalValue = 5,
            onBackClick = {},
            onSaveGoal = {},
            isInitiallyLocked = true,
            lockedUntilDate = "5/12/26",
            lockedDaysRemaining = 30
        )
    }
}
