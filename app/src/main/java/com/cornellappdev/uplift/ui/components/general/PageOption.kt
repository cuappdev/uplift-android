package com.cornellappdev.uplift.ui.components.general

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
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
import com.cornellappdev.uplift.util.GRAY03
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.montserratFamily

@Composable
fun PageOption(
    @DrawableRes icon: Int,
    title: String,
    onClick: () -> Unit,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = onClick,
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(painterResource(id = icon), contentDescription = title, tint = Color.Unspecified)
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontFamily = montserratFamily,
                    fontWeight = FontWeight.Medium,
                    color = PRIMARY_BLACK
                )
            }
            Icon(
                painterResource(id = R.drawable.chevron_right),
                contentDescription = null,
                tint = GRAY03
            )
        }
}

@Preview
@Composable
fun PageOptionPreview() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            Modifier.padding(16.dp)
        ) {
            PageOption(
            icon = R.drawable.ic_appdev,
            title = "About Uplift",
            onClick = {},
        )
        }

    }

}
