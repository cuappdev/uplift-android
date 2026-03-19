package com.cornellappdev.uplift.ui.components.profile

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.ui.components.onboarding.PhotoPicker
import com.cornellappdev.uplift.ui.components.onboarding.ScreenType
import com.cornellappdev.uplift.util.GRAY04
import com.cornellappdev.uplift.util.montserratFamily

@Composable
fun ProfileHeaderSection(
    name: String,
    gymDays: Int,
    streaks: Int,
    netId: String,
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
        ProfileHeaderInfoDisplay(name, gymDays, streaks, netId, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun ProfileHeaderInfoDisplay(
    name: String,
    gymDays: Int,
    streaks: Int,
    netID: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                modifier = Modifier.weight(1f, fill = false),
                fontFamily = montserratFamily,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (netID.isNotBlank()){
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "($netID)",
                    fontFamily = montserratFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ProfileHeaderInfo(
                label = "Gym Days",
                amount = gymDays
            )
            Spacer(modifier = Modifier.width(36.dp))
            ProfileHeaderInfo(
                label = "Streaks",
                amount = streaks
            )
//            ProfileHeaderInfo(
//                label = "Badges",
//                amount = badges
//            )
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
        name = "Melissa Velasquez",
        gymDays = 100,
        streaks = 15,
        profilePictureUri = null,
        onPhotoSelected = {},
        netId = "mv477"
    )
}