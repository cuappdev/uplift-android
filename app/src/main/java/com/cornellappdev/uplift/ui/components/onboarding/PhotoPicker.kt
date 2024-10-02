package com.cornellappdev.uplift.ui.components.onboarding

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.R

/***
 * Component that displays a camera icon for the user to select a photo for their profile
 *  @param onPhotoSelected: function to call when the user selects a photo. Takes in uri parameter
 */
@Composable
fun PhotoPicker(imageUri: Uri? = null, onPhotoSelected: (Uri) -> Unit) {
    // State to store the selected image URI
    var selectedImageUri by rememberSaveable { mutableStateOf<Uri?>(imageUri) }

    // Registers a photo picker activity launcher in single-select mode.
    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                selectedImageUri = uri
                onPhotoSelected(uri)
            }
        }

    Box(
        modifier = Modifier.size(160.dp), contentAlignment = Alignment.Center
    ) {
        ElevatedCard(elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ), colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ), shape = CircleShape, modifier = Modifier.size(160.dp), onClick = {
            // Launch the photo picker and let the user choose only images.
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                // Check if a photo is selected, and display it, otherwise show the camera icon
                if (selectedImageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(selectedImageUri),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(144.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(144.dp)
                            .clip(CircleShape)
                            .background(GRAY01),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_camera),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 16.dp)
                        )
                    }
                }
            }
        }
        // If a photo is selected, display the camera icon on top of the photo
        if (selectedImageUri != null) {
            Image(
                painter = painterResource(id = R.drawable.ic_camera_circle),
                contentDescription = null,
                modifier = Modifier.offset(60.dp, 64.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PhotoPickerPreview() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PhotoPicker {}
    }
}