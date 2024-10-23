package com.cornellappdev.uplift.ui.components.general

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.montserratFamily
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.shimmer

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
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Bottom),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showIcon) {
                // TODO: Change to display actual user's icon.
                AsyncImage(
                    model = "https://avatars.githubusercontent.com/u/47724806?v=4",
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

/**
 * A variant of the top bar that displays no information and shimmers.
 */
@Composable
fun LoadingTopBar(shimmerInstance: Shimmer) {
    TopAppBar(
        contentPadding = PaddingValues(24.dp),
        backgroundColor = GRAY01,
        elevation = 4.dp,
        modifier = Modifier.shimmer(shimmerInstance)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Bottom),
            verticalAlignment = Alignment.Bottom
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color.White,
                modifier = Modifier.size(width = 186.dp, height = 20.dp)
            ) {}
            Spacer(Modifier.weight(1f))
        }
    }
}

/**
 * A variant of the top bar that displays a central title and has a back arrow button
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpliftTopBarWithBack(
    title: String,
    onBackClick: () -> Unit = {},
    withBack: Boolean = true
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xfff8fafa)
        ),
        title = {
            Text(
                title, fontFamily = montserratFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp

            )
        },
        navigationIcon = {
            if (withBack) {
                IconButton(onClick = { onBackClick() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Localized description"
                    )
                }
            }
        },
    )
}
