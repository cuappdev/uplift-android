package com.cornellappdev.uplift.ui.components.general

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.montserratFamily

/**
 * A general-purpose top bar for use in several screens across Uplift.
 */
@Composable
fun UpliftTopBar(
    showIcon: Boolean = false,
    title: String,
    rightContent: @Composable () -> Unit = {}
) {
    TopAppBar(
        contentPadding = PaddingValues(16.dp),
        backgroundColor = Color.White,
        elevation = 20.dp
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            if (showIcon) {
                // TODO: Change to display actual user's icon.
                AsyncImage(
                    model = "https://avatars.cloudflare.steamstatic.com/10f7b0cd135382cb3f9c9eba9b689e5514795f91_full.jpg",
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                )
                Spacer(Modifier.width(12.dp))
            }
            Text(
                text = title,
                fontWeight = FontWeight(700),
                fontSize = 24.sp,
                lineHeight = 29.26.sp,
                color = PRIMARY_BLACK,
                fontFamily = montserratFamily
            )
            Spacer(Modifier.weight(1f))
            rightContent()
        }
    }
}