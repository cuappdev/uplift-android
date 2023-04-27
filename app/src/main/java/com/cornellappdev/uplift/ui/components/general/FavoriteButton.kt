package com.cornellappdev.uplift.ui.components.general

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cornellappdev.uplift.R

/**
 * Favorite Button used for various places in Uplift, such as the Home Card and Gym Detail.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FavoriteButton(filled: Boolean, onClick: () -> Unit) {
    Surface(
        shape = CircleShape,
        color = Color.White,
        modifier = Modifier.size(32.dp),
        onClick = onClick
    ) {
        Image(
            painter = painterResource(id = if (filled) R.drawable.ic_star_filled else R.drawable.ic_star),
            contentDescription = null,
            modifier = Modifier.padding(4.dp)
        )
    }
}