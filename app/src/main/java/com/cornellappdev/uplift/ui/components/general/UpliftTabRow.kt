package com.cornellappdev.uplift.ui.components.general

import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.GRAY04
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.PRIMARY_YELLOW
import com.cornellappdev.uplift.util.montserratFamily

@Composable
fun UpliftTabRow(tabIndex: Int, tabs: List<String>, onTabChange: (Int) -> Unit = {}) {
    TabRow(
        selectedTabIndex = tabIndex,
        containerColor = Color.White,
        indicator = { tabPositions ->
            SecondaryIndicator(
                Modifier.tabIndicatorOffset(tabPositions[tabIndex]),
                height = 2.dp,
                color = PRIMARY_YELLOW
            )
        },
        divider = {
            Divider(
                color = GRAY01,
                thickness = 1.dp,
            )
        }
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                text = {
                    Text(
                        text = title,
                        color = if (tabIndex == index) PRIMARY_BLACK else GRAY04,
                        fontFamily = montserratFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                selected = tabIndex == index,
                onClick = { onTabChange(index) },
                selectedContentColor = GRAY01,
            )
        }
    }
}