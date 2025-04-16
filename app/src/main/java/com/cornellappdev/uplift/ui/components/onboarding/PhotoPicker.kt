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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.R

data class PhotoPickerSizes(
    val outerCircleSize: Dp,
    val innerCircleSize: Dp,
    val cameraIconOffsetX: Dp,
    val cameraIconOffsetY: Dp
)

fun getPhotoPickerSizes(screenType: String): PhotoPickerSizes {
    return when (screenType) {
        "onboarding" -> PhotoPickerSizes(
            outerCircleSize = 160.dp,
            innerCircleSize = 144.dp,
            cameraIconOffsetX = 60.dp,
            cameraIconOffsetY = 64.dp
        )
        "profile" -> PhotoPickerSizes(
            outerCircleSize = 98.dp,
            innerCircleSize = 88.dp,
            cameraIconOffsetX = 36.dp,
            cameraIconOffsetY = 40.dp
        )
        else -> PhotoPickerSizes(
            outerCircleSize = 160.dp,
            innerCircleSize = 144.dp,
            cameraIconOffsetX = 60.dp,
            cameraIconOffsetY = 64.dp
        )
    }
}

@Composable
fun PhotoPicker(imageUri: Uri? = null, onPhotoSelected: (Uri) -> Unit, screenType: String = "onboarding") {
    val sizes = getPhotoPickerSizes(screenType)

    // State to store the selected image URI
    var selectedImageUri by rememberSaveable { mutableStateOf(imageUri) }

    // Registers a photo picker activity launcher in single-select mode.
    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                selectedImageUri = uri
                onPhotoSelected(uri)
            }
        }

    Box(
        modifier = Modifier.size(sizes.outerCircleSize), contentAlignment = Alignment.Center
    ) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = CircleShape,
            modifier = Modifier.size(sizes.outerCircleSize),
            onClick = {
                pickMedia.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (selectedImageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(selectedImageUri),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(sizes.innerCircleSize)
                            .clip(CircleShape)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(sizes.innerCircleSize)
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
        if (selectedImageUri != null || screenType == "profile") {
            Image(
                painter = painterResource(id = R.drawable.ic_camera_circle),
                contentDescription = null,
                modifier = Modifier
                    .offset(sizes.cameraIconOffsetX, sizes.cameraIconOffsetY)
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
        PhotoPicker(
            imageUri = null,
            onPhotoSelected = { }
        )
    }
}