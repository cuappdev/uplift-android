package com.cornellappdev.uplift.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.models.UpliftClass
import com.cornellappdev.uplift.nav.navigateToClass
import com.cornellappdev.uplift.ui.viewmodels.ClassDetailViewModel
import com.cornellappdev.uplift.util.*
import java.util.*

/**
 * A card component displaying information about [thisClass].
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ClassInfoCard(
    thisClass: UpliftClass,
    navController: NavHostController,
    classDetailViewModel: ClassDetailViewModel
) {
    val today = Calendar.getInstance()

    Surface(
        shape = RoundedCornerShape(5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .border(width = 1.dp, brush = SolidColor(GRAY01), shape = RoundedCornerShape(5.dp)),
        color = Color.White,
        onClick = {
            navController.navigateToClass(
                classDetailViewModel = classDetailViewModel,
                thisClass = thisClass
            )
        }
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
                Row(modifier = Modifier.weight(1f)) {
                    Column {
                        Text(
                            text = if (today.get(Calendar.MONTH) == thisClass.date.get(Calendar.MONTH) &&
                                today.get(Calendar.DAY_OF_MONTH) == thisClass.date.get(Calendar.DAY_OF_MONTH)
                            ) "Today" else calendarDayToString(thisClass.date),
                            fontFamily = montserratFamily,
                            fontSize = 14.sp,
                            fontWeight = FontWeight(500),
                            lineHeight = 17.07.sp,
                            textAlign = TextAlign.Center,
                            color = PRIMARY_BLACK
                        )
                        Text(
                            text = thisClass.time.start.toString(),
                            fontFamily = montserratFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight(400),
                            lineHeight = 14.63.sp,
                            textAlign = TextAlign.Center,
                            color = PRIMARY_BLACK
                        )
                    }

                    Spacer(modifier = Modifier.width(32.dp))

                    Column {
                        Text(
                            text = thisClass.name,
                            fontFamily = montserratFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight(500),
                            lineHeight = 17.07.sp,
                            color = PRIMARY_BLACK,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = thisClass.location,
                            fontFamily = montserratFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight(400),
                            lineHeight = 14.63.sp,
                            textAlign = TextAlign.Center,
                            color = PRIMARY_BLACK
                        )

                        Spacer(modifier = Modifier.height(17.dp))

                        Text(
                            text = thisClass.instructorName,
                            fontFamily = montserratFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight(400),
                            lineHeight = 14.63.sp,
                            textAlign = TextAlign.Center,
                            color = GRAY03
                        )
                    }
                }

                Image(
                    painter = painterResource(
                        id = if (thisClass.isFavorite()) R.drawable.ic_star_black_filled
                        else R.drawable.ic_star_black
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null
                        ) {
                            thisClass.toggleFavorite()
                        }
                )
            }
        }
    }
}
