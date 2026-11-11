package com.cornellappdev.uplift.ui.components.goalsetting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.util.GRAY08
import com.cornellappdev.uplift.util.montserratFamily
import kotlinx.coroutines.launch
import kotlin.math.abs

/**
 * @param initialIsAm: whether the initial selection is AM
 * @param onValueChanged: callback for when the selection changes
 * @return AmPmSelector composable that allows the user to select AM or PM
 */
@Composable
fun AmPmSelector(
    initialIsAm: Boolean, onValueChanged: (Boolean) -> Unit
) {
    val items = listOf("AM", "PM")
    val itemHeight = 24.dp
    val scope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()
    var selectedIndex by remember { mutableIntStateOf(if (initialIsAm) 0 else 1) }
    val density = LocalDensity.current

    LaunchedEffect(Unit) {
        scrollState.scrollToItem(if (initialIsAm) 0 else 1)
    }
    Box(
        modifier = Modifier.height(itemHeight * 3)
    ) {
        LazyColumn(
            state = scrollState,
            modifier = Modifier.width(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            userScrollEnabled = true
        ) {
            // Top spacer
            item {
                Spacer(modifier = Modifier.height(itemHeight))
            }

            items(items.size) { index ->
                val isSelected = selectedIndex == index
                Box(
                    modifier = Modifier
                        .height(itemHeight)
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            val center = coordinates.size.height / 2f
                            val position = coordinates.positionInParent().y + center
                            val totalHeight = with(density) { (itemHeight * 3).toPx() }
                            val middlePosition = totalHeight / 2f

                            if (abs(position - middlePosition) <
                                with(density) { itemHeight.toPx() } / 2
                            ) {
                                if (selectedIndex != index) {
                                    selectedIndex = index
                                    onValueChanged(index == 0)
                                    scope.launch {
                                        scrollState.animateScrollToItem(index)
                                    }
                                }
                            }
                        }, contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = items[index],
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.Black else GRAY08,
                        fontFamily = montserratFamily
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(itemHeight))
            }
        }
    }
}