package com.cornellappdev.uplift.ui.components.profile.checkin

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.theme.AppColors
import com.cornellappdev.uplift.ui.theme.AppTextStyles

@Composable
fun CheckInPrompt(
    gymName: String,
    currentTimeText: String,
    onDismiss: () -> Unit,
    onCheckIn: () -> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
        ) {
            Text(
                text = "We see you're near a gym...",
                style = AppTextStyles.BodySemibold,
                color = AppColors.Black
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "$gymName at $currentTimeText",
                style = AppTextStyles.LabelNormal,
                color = AppColors.Black
            )
        }
        Row(
            modifier = Modifier
                .height(34.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                modifier = Modifier
                    .width(93.dp)
                    .height(34.dp),
                shape = RoundedCornerShape(size = 11.05263.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = AppColors.LightYellow,
                    contentColor = AppColors.Black
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
                onClick = onCheckIn
            ) {
                Text(
                    text = "Check In?",
                    style = AppTextStyles.LabelBig,
                    color = AppColors.Black
                )
            }

            Image(
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = "close pop up",
                contentScale = ContentScale.None,
                modifier = Modifier.clickable { onDismiss() }
            )
        }


    }
}

@Preview(showBackground = true)
@Composable
private fun CheckInPromptPreview() {
    CheckInPrompt("Helen Newman", "1:00 PM", {}, {})
}