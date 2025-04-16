package com.cornellappdev.uplift.ui.components.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.util.GRAY10
import com.cornellappdev.uplift.util.montserratFamily

@Composable
fun SectionTitleText(
    title: String,
    onClick : () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = null
            )
        ,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontFamily = montserratFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = GRAY10
        )
        Icon(
            painter = painterResource(id = R.drawable.chevron_right),
            tint = GRAY10,
            contentDescription = "View all history",
            modifier = Modifier
                .width(18.dp)
                .height(18.dp),
        )
    }
}