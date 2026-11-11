package com.cornellappdev.uplift.ui.components.general

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.GRAY04
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.PRIMARY_YELLOW
import com.cornellappdev.uplift.util.montserratFamily

@Composable
fun UpliftTabRow(
    tabIndex: Int,
    tabs: List<String>,
    icons: List<Int>? = null,
    onTabChange: (Int) -> Unit = {}
) {
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
            val isSelected = tabIndex == index
            val color = if (isSelected) PRIMARY_BLACK else GRAY04
            Tab(
                selected = isSelected,
                onClick = { onTabChange(index) },
                selectedContentColor = GRAY01,
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (icons != null && index < icons.size) {
                            Icon(
                                painter = painterResource(id = icons[index]),
                                contentDescription = null,
                                tint = color,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                        }
                        Text(
                            text = title,
                            color = color,
                            fontFamily = montserratFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        }
    }
}

@Preview
@Composable
private fun UpliftTabRowPreview() {
    var tabIndex by remember { mutableStateOf(0) }
    UpliftTabRow(
        tabIndex = tabIndex,
        tabs = listOf("Gym", "Classes"),
        onTabChange = {
            tabIndex = it
        }
    )
}
