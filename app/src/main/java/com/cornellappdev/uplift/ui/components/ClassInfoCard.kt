package com.cornellappdev.uplift.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.GRAY03
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.montserratFamily

@Preview
@Composable
fun ClassInfoCard() {
    Surface(
        shape = RoundedCornerShape(5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .border(width = 1.dp, brush = SolidColor(GRAY01), shape = RoundedCornerShape(5.dp))
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row {
                    Column {
                        Text(
                            text = "Today",
                            fontFamily = montserratFamily,
                            fontSize = 14.sp,
                            fontWeight = FontWeight(500),
                            lineHeight = 17.07.sp,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "10:30 AM",
                            fontFamily = montserratFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight(400),
                            lineHeight = 14.63.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.width(32.dp))

                    Column() {
                        Text(
                            text = "Muscle Pump",
                            fontFamily = montserratFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight(500),
                            lineHeight = 17.07.sp,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Teagle Multipurpose Room",
                            fontFamily = montserratFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight(400),
                            lineHeight = 14.63.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(17.dp))

                        Text(
                            text = "Claire M.",
                            fontFamily = montserratFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight(400),
                            lineHeight = 14.63.sp,
                            textAlign = TextAlign.Center,
                            color = GRAY03
                        )
                    }
                }

                Icon(
                    painter = painterResource(id = R.drawable.ic_star),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = PRIMARY_BLACK
                )
            }
        }
    }
}