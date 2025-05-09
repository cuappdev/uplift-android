package com.cornellappdev.uplift.ui.components.profile

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
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
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.components.onboarding.PhotoPicker
import com.cornellappdev.uplift.ui.components.onboarding.ScreenType
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.GRAY04
import com.cornellappdev.uplift.util.montserratFamily

@Composable
fun ProfileHeaderSection(
    name: String,
    gymDays: Int,
    streaks: Int,
    badges: Int,
    profilePictureUri: Uri?,
    onPhotoSelected: (Uri) -> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        PhotoPicker(
            imageUri = profilePictureUri,
            onPhotoSelected = onPhotoSelected,
            screenType = ScreenType.PROFILE
        )
        ProfileHeaderInfoDisplay(name, gymDays, streaks, badges)
    }
}

@Composable
private fun ProfileHeaderInfoDisplay(
    name: String,
    gymDays: Int,
    streaks: Int,
    badges: Int
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = name,
            fontFamily = montserratFamily,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ProfileHeaderInfo(
                label = "Gym Days",
                amount = gymDays
            )
            ProfileHeaderInfo(
                label = "Streaks",
                amount = streaks
            )
            ProfileHeaderInfo(
                label = "Badges",
                amount = badges
            )
        }
    }
}

@Composable
private fun ProfileHeaderInfo(label: String, amount: Int) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = amount.toString(),
            fontFamily = montserratFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            fontFamily = montserratFamily,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = GRAY04
        )

    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileHeaderSectionPreview() {
    ProfileHeaderSection(
        name = "John Doe",
        gymDays = 100,
        streaks = 15,
        badges = 3,
        profilePictureUri = null,
        onPhotoSelected = {}
    )
}