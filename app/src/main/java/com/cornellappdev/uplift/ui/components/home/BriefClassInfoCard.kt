package com.cornellappdev.uplift.ui.components.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.models.UpliftClass
import com.cornellappdev.uplift.ui.viewmodels.ClassDetailViewModel
import com.cornellappdev.uplift.util.*

/**
 * A component displaying brief information as to a [UpliftClass]. Used on the home screen.
 */
@Composable
fun BriefClassInfoCard(
    thisClass: UpliftClass,
    navController: NavController,
    classDetailViewModel: ClassDetailViewModel
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color.White,
        modifier = Modifier
            .widthIn(min = 228.dp)
            .clickable {
                classDetailViewModel.selectClass(thisClass)
                navController.navigate("classDetail")
            },
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .padding(start = 12.dp, top = 8.dp, bottom = 12.dp, end = 12.dp)
        ) {
            Text(
                text = thisClass.name,
                fontFamily = montserratFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight(500),
                lineHeight = 19.5.sp,
                color = PRIMARY_BLACK,
                modifier = Modifier.padding(bottom = 2.dp)
            )
            Text(
                text = thisClass.time.toString(),
                fontFamily = montserratFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight(400),
                lineHeight = 14.63.sp,
                color = GRAY05,
                modifier = Modifier.padding(bottom = 2.dp)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_map_pin),
                    contentDescription = null,
                    tint = GRAY02,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    text = thisClass.location,
                    fontFamily = montserratFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight(400),
                    lineHeight = 15.sp,
                    color = GRAY03
                )
            }
        }
    }
}