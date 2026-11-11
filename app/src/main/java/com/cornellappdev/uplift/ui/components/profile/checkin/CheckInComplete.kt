package com.cornellappdev.uplift.ui.components.profile.checkin

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.theme.AppColors
import com.cornellappdev.uplift.ui.theme.AppTextStyles

@Composable
fun CheckInComplete(
    onClosePopUp: () -> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "You’re all set. Enjoy your workout!",
            style = AppTextStyles.BodySemibold,
            color = AppColors.Black
        )
        Image(
            painter = painterResource(id = R.drawable.ic_close),
            contentDescription = "close pop up",
            contentScale = ContentScale.None,
            modifier = Modifier.clickable { onClosePopUp() }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CheckInCompletePreview(){
    CheckInComplete({})
}
