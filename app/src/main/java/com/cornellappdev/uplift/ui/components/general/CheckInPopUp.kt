package com.cornellappdev.uplift.ui.components.general

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.uplift.ui.components.profile.checkin.CheckInComplete
import com.cornellappdev.uplift.ui.components.profile.checkin.CheckInPrompt
import com.cornellappdev.uplift.ui.theme.AppColors
import com.cornellappdev.uplift.ui.viewmodels.profile.CheckInMode


@Composable
fun CheckInPopUp(
    gymName: String,
    currentTimeText: String,
    onDismiss: () -> Unit,
    onCheckIn: () -> Unit,
    onClosePopUp: () -> Unit,
    mode: CheckInMode
) {
    Row(
        modifier = Modifier
            .shadow(
                elevation = 40.dp,
                spotColor = AppColors.Gray01,
                ambientColor = AppColors.Gray01
            )
            .fillMaxWidth()
            .height(62.dp)
            .background(color = AppColors.White, shape = RoundedCornerShape(size = 12.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AnimatedContent(
            targetState = mode,
            transitionSpec = {
                (fadeIn(tween(300)) togetherWith fadeOut(tween(300)))
                    .using(SizeTransform(false))
            },
            label = "check-in"
        ) { target ->
            when (target) {
                CheckInMode.Prompt -> CheckInPrompt(
                    gymName = gymName,
                    currentTimeText = currentTimeText,
                    onCheckIn = onCheckIn,
                    onDismiss = onDismiss
                )
                CheckInMode.Complete -> CheckInComplete(
                    onClosePopUp = onClosePopUp
                )
            }

        }
    }
}

@Preview
@Composable
private fun CheckInPopUpPreview(
){
    CheckInPopUp("Helen Newman",
   "1:00 PM", {}, {}, {}, CheckInMode.Complete)
}