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
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.GRAY04
import com.cornellappdev.uplift.util.montserratFamily

@Composable
fun ProfileHeaderSection(
    name: String,
    totalWorkouts: Int,
    navigateToFavorites: () -> Unit,
    profilePictureUri: Uri?
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        PhotoPicker(
            imageUri = profilePictureUri,
            onPhotoSelected = {},
            screenType = "profile"
        )
        ProfileHeaderInfoDisplay(name, totalWorkouts, navigateToFavorites)
    }
}

@Composable
private fun ProfileHeaderInfoDisplay(
    name: String,
    totalWorkouts: Int,
    navigateToFavorites: () -> Unit
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
            TotalWorkoutsInfo(totalWorkouts)
            FavoritesButton(navigateToFavorites)
        }
    }
}

@Composable
private fun FavoritesButton(navigateToFavorites: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(38.dp),
        border = BorderStroke(1.dp, GRAY01),
        color = Color.White,
        modifier = Modifier.clickable(
            onClick = navigateToFavorites
        )
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_star_black_filled),
                contentDescription = "Favorites",
                tint = Color.Unspecified,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = "Favorites",
                fontFamily = montserratFamily,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun TotalWorkoutsInfo(totalWorkouts: Int) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Total Workouts",
            fontFamily = montserratFamily,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = GRAY04
        )
        Text(
            text = totalWorkouts.toString(),
            fontFamily = montserratFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileHeaderSectionPreview() {
    ProfileHeaderSection(
        name = "John Doe",
        totalWorkouts = 100,
        navigateToFavorites = {},
        profilePictureUri = null
    )
}