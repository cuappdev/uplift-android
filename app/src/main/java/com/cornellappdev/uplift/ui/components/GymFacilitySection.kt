package com.cornellappdev.uplift.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.models.Gym
import com.cornellappdev.uplift.ui.screens.LineSpacer
import com.cornellappdev.uplift.util.ACCENT_CLOSED
import com.cornellappdev.uplift.util.ACCENT_OPEN
import com.cornellappdev.uplift.util.montserratFamily

@Composable
fun GymFacilitySection(gym: Gym, today: Int) {
    var openedFacility by remember { mutableStateOf(-1) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "FACILITIES",
            fontFamily = montserratFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight(700),
            lineHeight = 19.5.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        // Equipment Tab
        FacilityTab(
            painterResource(id = R.drawable.ic_dumbbell),
            "EQUIPMENT",
            openedFacility != 1,
            onClick = {
                openedFacility = if (openedFacility == 1) -1 else 1
            },
            open = OpenType.NOT_APPLICABLE
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp, top = 12.dp)
                    .horizontalScroll(
                        rememberScrollState()
                    )
            ) {
                Spacer(modifier = Modifier.width(24.dp))
                for (group in gym.equipmentGroupings) {
                    EquipmentGroup(group = group)
                    Spacer(modifier = Modifier.width(10.dp))
                }
                Spacer(modifier = Modifier.width(14.dp))
            }
        }

        LineSpacer(paddingStart = 24.dp, paddingEnd = 24.dp)

        FacilityTab(
            painterResource(id = R.drawable.ic_basketball_hoop),
            "GYMNASIUM",
            openedFacility != 2,
            onClick = {
                openedFacility = if (openedFacility == 2) -1 else 2
            },
            open = OpenType.OPEN
        ) {
            DayOfWeekSelector(today = today) { day ->

            }
        }

        LineSpacer(paddingStart = 24.dp, paddingEnd = 24.dp)

        FacilityTab(
            painterResource(id = R.drawable.ic_swimming_pool),
            "SWIMMING POOL",
            openedFacility != 3,
            onClick = {
                openedFacility = if (openedFacility == 3) -1 else 3
            },
            open = OpenType.OPEN
        ) {
            Spacer(Modifier.height(5.dp))
            GymSwimmingSection(today = today, gym = gym)
        }

        LineSpacer(paddingStart = 24.dp, paddingEnd = 24.dp)

        FacilityTab(
            painterResource(id = R.drawable.ic_bowling_pins),
            "BOWLING LANES",
            openedFacility != 4,
            onClick = {
                openedFacility = if (openedFacility == 4) -1 else 4
            },
            open = OpenType.CLOSED
        ) {
            GymBowlingSection(today = today, gym = gym)
        }

        LineSpacer(paddingStart = 24.dp, paddingEnd = 24.dp)

        FacilityTab(
            painterResource(id = R.drawable.ic_miscellaneous_dots),
            "MISCELLANEOUS",
            openedFacility != 5,
            onClick = {
                openedFacility = if (openedFacility == 5) -1 else 5
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
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }

        LineSpacer(paddingStart = 24.dp, paddingEnd = 24.dp)


    }
}

enum class OpenType {
    NOT_APPLICABLE, OPEN, CLOSED
}

@Composable
fun FacilityTab(
    painter: Painter,
    title: String,
    collapsed: Boolean,
    onClick: () -> Unit,
    open : OpenType,
    content: @Composable () -> Unit
) {
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
                modifier = Modifier.padding(start = 8.dp)
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
                modifier = Modifier.padding(end = 24.dp)
            )
        }

        if (!collapsed)
            content()
    }
}