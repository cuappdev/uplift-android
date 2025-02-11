package com.cornellappdev.uplift.ui.components.gymdetail

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.data.models.gymdetail.OpenType
import com.cornellappdev.uplift.data.models.UpliftGym
import com.cornellappdev.uplift.ui.components.GymBowlingSection
import com.cornellappdev.uplift.ui.components.GymCourtSection
import com.cornellappdev.uplift.ui.components.GymSwimmingSection
import com.cornellappdev.uplift.ui.screens.LineSpacer
import com.cornellappdev.uplift.util.ACCENT_CLOSED
import com.cornellappdev.uplift.util.ACCENT_OPEN
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.isOpen
import com.cornellappdev.uplift.util.montserratFamily

/**
 * Displays the "FACILITIES" for a gym. Includes information on Equipment, Gymnasiums, Swimming,
 * Bowling, and Miscellaneous info pertaining to a gym.
 */
@Composable
fun GymFacilitySection(gym: UpliftGym, today: Int) {
    var openedFacility by remember { mutableIntStateOf(-1) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        // TODO: Change to court-by-court design.
        gym.courtInfo.forEachIndexed { i, court ->
            FacilityTab(
                painterResource(id = R.drawable.ic_basketball_hoop),
                title = court.name,
                openedFacility != 5 + i,
                onClick = {
                    openedFacility = if (openedFacility == 5 + i) -1 else 5 + i
                },
                open = when (isOpen(court.hours[today]?.map { it.time })) {
                    true -> OpenType.OPEN
                    else -> OpenType.CLOSED
                }
            ) {
                GymCourtSection(today = today, court = court)
            }

            LineSpacer(paddingStart = 24.dp, paddingEnd = 24.dp)
        }

        if (gym.swimmingInfo != null) {
            FacilityTab(
                painterResource(id = R.drawable.ic_swimming_pool),
                "SWIMMING POOL",
                openedFacility != 2,
                onClick = {
                    openedFacility = if (openedFacility == 2) -1 else 2
                },
                open = when (gym.swimmingInfo[today] != null
                        && isOpen(gym.swimmingInfo[today]!!.hours())) {
                    true -> OpenType.OPEN
                    else -> OpenType.CLOSED
                }
            ) {
                Spacer(Modifier.height(5.dp))
                GymSwimmingSection(today = today, gym = gym)
            }

            LineSpacer(paddingStart = 24.dp, paddingEnd = 24.dp)
        }

        if (gym.bowlingInfo != null) {
            FacilityTab(
                painterResource(id = R.drawable.ic_bowling_pins),
                "BOWLING LANES",
                openedFacility != 3,
                onClick = {
                    openedFacility = if (openedFacility == 3) -1 else 3
                },
                open = when (gym.bowlingInfo[today] != null
                        && isOpen(gym.bowlingInfo[today]!!.hours)) {
                    true -> OpenType.OPEN
                    else -> OpenType.CLOSED
                }
            ) {
                GymBowlingSection(today = today, gym = gym)
            }

            LineSpacer(paddingStart = 24.dp, paddingEnd = 24.dp)
        }

        if (gym.miscellaneous.isNotEmpty()) {
            FacilityTab(
                painterResource(id = R.drawable.ic_miscellaneous_dots),
                "MISCELLANEOUS",
                openedFacility != 4,
                onClick = {
                    openedFacility = if (openedFacility == 4) -1 else 4
                },
                open = OpenType.NOT_APPLICABLE
            ) {
                Column(modifier = Modifier.padding(start = 46.dp, bottom = 12.dp)) {
                    Spacer(Modifier.height(2.dp))
                    for (misc in gym.miscellaneous) {
                        Text(
                            text = misc,
                            fontFamily = montserratFamily,
                            fontSize = 14.sp,
                            fontWeight = FontWeight(400),
                            lineHeight = 20.sp,
                            textAlign = TextAlign.Left,
                            modifier = Modifier.padding(start = 8.dp),
                            color = PRIMARY_BLACK
                        )
                    }
                }
            }

            LineSpacer(paddingStart = 24.dp, paddingEnd = 24.dp)
        }
    }
}

/**
 * One facility tab which can contain some Composable content that may be expanded upon a tap.
 *
 * @param painter The icon which this facility tab should display.
 */
@Composable
fun FacilityTab(
    painter: Painter,
    title: String,
    collapsed: Boolean,
    onClick: () -> Unit,
    open: OpenType,
    content: @Composable () -> Unit
) {
    val rotationAnimation by animateFloatAsState(targetValue = if (collapsed) 0f else 90f)

    Column(modifier = Modifier.animateContentSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clickable {
                    onClick()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .padding(start = 24.dp)
                    .size(24.dp),
                painter = painter,
                contentDescription = null,
                tint = Color.Black
            )
            Text(
                text = title,
                fontFamily = montserratFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight(500),
                lineHeight = 19.5.sp,
                textAlign = TextAlign.Left,
                modifier = Modifier.padding(start = 8.dp),
                color = PRIMARY_BLACK
            )
            Spacer(modifier = Modifier.weight(1f))
            if (open != OpenType.NOT_APPLICABLE)
                Text(
                    text = if (open == OpenType.OPEN) "Open" else "Closed",
                    fontFamily = montserratFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight(500),
                    lineHeight = 19.5.sp,
                    textAlign = TextAlign.Left,
                    modifier = Modifier.padding(end = 16.dp),
                    color = if (open == OpenType.OPEN) ACCENT_OPEN else ACCENT_CLOSED
                )
            Icon(
                painter = painterResource(id = R.drawable.ic_caret_right),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 24.dp)
                    .rotate(rotationAnimation),
                tint = PRIMARY_BLACK
            )
        }

        if (!collapsed)
            content()
    }
}
