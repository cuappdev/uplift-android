package com.cornellappdev.uplift.ui.components.general

import androidx.compose.animation.core.Spring.DampingRatioLowBouncy
import androidx.compose.animation.core.Spring.StiffnessMediumLow
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.PRIMARY_YELLOW
import com.cornellappdev.uplift.util.montserratFamily

/**
 * A general tab content selector for Uplift. Takes in a sequence of pairs, where each pair becomes
 * a label. A pair's first is the tab label, and the second is the Composable content to show
 * when that tab is selected.
 */
@Composable
fun TabContentSelector(tabs: List<Pair<String, @Composable () -> Unit>>) {
    var selectedTab by remember { mutableIntStateOf(0) }

    val labels = tabs.map { it.first }
    val composables = tabs.map { it.second }
    val composableToShow = composables[selectedTab]

    val n = labels.size.toFloat()
    val leftWeightState = animateFloatAsState(
        targetValue = selectedTab / n,
        animationSpec = spring(DampingRatioLowBouncy, StiffnessMediumLow),
        label = "yellow_bar"
    )
    val leftWeight = leftWeightState.value
    val rightWeight = (n - 1) / n - leftWeight

    Column(modifier = Modifier.fillMaxWidth()) {
        // Tabs.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
        ) {
            Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                // Make each tab.
                for (i in labels.indices) {
                    val interaction = remember {
                        MutableInteractionSource()
                    }

                    val fontWeight =
                        animateIntAsState(
                            targetValue = if (i == selectedTab) 700 else 500,
                            label = "label_weight"
                        )

                    val label = labels[i]
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable(
                                interactionSource = interaction,
                                indication = ripple()
                            ) {
                                selectedTab = i
                            }, contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            fontFamily = montserratFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight(fontWeight.value),
                            lineHeight = 16.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = PRIMARY_BLACK
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .background(GRAY01)
                    .height(1.dp)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {}

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                // Left weight
                if (leftWeight > 0)
                    Box(modifier = Modifier.weight(leftWeight)) {}

                // Actual Yellow Line
                Box(
                    modifier = Modifier
                        .background(PRIMARY_YELLOW)
                        .height(2.dp)
                        .weight(1 / tabs.size.toFloat())
                ) {}

                // Right weight
                if (rightWeight > 0)
                    Box(modifier = Modifier.weight(rightWeight)) {}
            }
        }

        // Content.
        Box(modifier = Modifier.fillMaxWidth()) {
            composableToShow()
        }
    }
}
