package com.cornellappdev.uplift.ui.components.gymdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.fragment.GymFields
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.gymdetail.amenityMap
import com.cornellappdev.uplift.util.montserratFamily

/**
 * Displays all of the Amenities that are included at the specific gym
 * @param amenities the list of amenities that the gym has (see [GymFields.Amenity])
 */
@Composable
fun GymAmenitySection(amenities: List<GymFields.Amenity?>?) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Amenities",
            fontFamily = montserratFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = PRIMARY_BLACK
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            amenities.orEmpty().filterNotNull().filter { it.type.rawValue in amenityMap }
                .map { amenity ->
                    val (drawableId, amenityName) = amenityMap[amenity.type.rawValue] ?: return@map
                    AmenityElement(
                        painter = painterResource(id = drawableId),
                        name = amenityName
                    )
                }
        }
    }
}


/**
 * A single amenity composable content
 *
 * @param painter The icon which this facility tab should display.
 * @param name The name of the facility tab.
 */
@Composable
fun AmenityElement(painter: Painter, name: String) {
    Surface(
        modifier = Modifier
            .wrapContentSize(),
        color = Color.White,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier
                    .size(24.dp),
                painter = painter,
                contentDescription = null,
                tint = PRIMARY_BLACK
            )
            Text(
                text = name,
                fontFamily = montserratFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 19.5.sp,
                textAlign = TextAlign.Left,
                color = PRIMARY_BLACK
            )
        }
    }
}