package com.cornellappdev.uplift.ui.components.gymdetail

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter

import androidx.compose.ui.res.colorResource

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter.State.Empty.painter
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.fragment.GymFields
import com.cornellappdev.uplift.data.models.EquipmentGrouping
import com.cornellappdev.uplift.data.models.UpliftGym
import com.cornellappdev.uplift.ui.nav.navigateToClass
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.montserratFamily

/**
 * Displays all of the Amenities that are included at the specific gym
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GymAmenitySection(amenities: List<GymFields.Amenity?>?) {


    if (amenities != null) {
        Spacer(modifier = Modifier.height(8.dp))
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            maxItemsInEachRow = 3,
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.spacedBy(8.dp)


        ) {
            // Define a map to map amenity types to drawable resources and names
            val amenityMap = mapOf(
                "SHOWERS" to Pair(R.drawable.ic_shower, "Showers"),
                "LOCKERS" to Pair(R.drawable.ic_lock, "Lockers"),
                "PARKING" to Pair(R.drawable.ic_parking, "Parking"),
                "ELEVATORS" to Pair(R.drawable.ic_elevator, "Elevators/Lifts")
            )

            // Iterate over amenities
            for (amenity in amenities) {
                if (amenity != null && amenity.type.rawValue in amenityMap) {
                    val (drawableId, amenityName) = amenityMap[amenity.type.rawValue]!!
                    AmenityElement(
                        painter = painterResource(id = drawableId),
                        name = amenityName
                    )
                }
            }
        }
    }
}


/**
 * A single amenity composable content
 *
 * @param painter The icon which this facility tab should display.
 */
@Composable
fun AmenityElement(painter: Painter, name: String) {
    Surface(
        modifier = Modifier
            .wrapContentSize()
            .padding(horizontal = 4.dp),
        shape = RoundedCornerShape(8.dp),
        color = GRAY01,
    ) {
        Row(
            Modifier
                .padding(
                    start = 6.dp,
                    top = 6.dp,
                    end = 10.dp,
                    bottom = 6.dp
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                modifier = Modifier
                    .size(18.dp),
                painter = painter,
                contentDescription = null,
                tint = Color.Black
            )
            Text(
                text = name,
                fontFamily = montserratFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight(500),
                lineHeight = 19.5.sp,
                textAlign = TextAlign.Left,
                modifier = Modifier.padding(start = 8.dp),
                color = PRIMARY_BLACK
            )
        }
    }
}