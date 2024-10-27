package com.cornellappdev.uplift.ui.components.goalsetting

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.GRAY08
import com.cornellappdev.uplift.util.montserratFamily
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun <T> ScrollWheel(
    width: Dp,
    itemHeight: Dp,
    numberOfDisplayedItems: Int = 3,
    items: List<T>,
    initialItem: T,
    itemScaleFact: Float = 1.5f,
    textStyle: TextStyle,
    textColor: Color,
    selectedTextColor: Color,
    onItemSelected: (index: Int, item: T) -> Unit = { _, _ -> },
) {
    val itemHalfHeight = LocalDensity.current.run { itemHeight.toPx() / 2f }
    val scrollState = rememberLazyListState(0)
    var lastSelectedIndex by remember {
        mutableIntStateOf(0)
    }
    var itemsState by remember {
        mutableStateOf(items)
    }
    LaunchedEffect(items) {
        var targetIndex = items.indexOf(initialItem) - 1
        targetIndex += ((Int.MAX_VALUE / 2) / items.size) * items.size
        itemsState = items
        lastSelectedIndex = targetIndex
        scrollState.scrollToItem(targetIndex)
    }
    LazyColumn(
        modifier = Modifier
            .width(width)
            .height(itemHeight * numberOfDisplayedItems)
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, GRAY01, RoundedCornerShape(8.dp)),
        state = scrollState,
        flingBehavior = rememberSnapFlingBehavior(
            lazyListState = scrollState
        )
    ) {
        items(count = Int.MAX_VALUE, itemContent = { i ->
            val item = itemsState[i % itemsState.size]
            Box(
                modifier = Modifier
                    .height(itemHeight)
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        val y = coordinates.positionInParent().y - itemHalfHeight
                        val parentHalfHeight = (itemHalfHeight * numberOfDisplayedItems)
                        val isSelected =
                            (y > parentHalfHeight - itemHalfHeight && y < parentHalfHeight + itemHalfHeight)
                        val index = i - 1
                        if (isSelected && lastSelectedIndex != index) {
                            onItemSelected(index % itemsState.size, item)
                            lastSelectedIndex = index
                        }
                    }, contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.toString(),
                    style = textStyle,
                    fontFamily = montserratFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = if (lastSelectedIndex == i) {
                        selectedTextColor
                    } else {
                        textColor
                    },
                    fontSize = if (lastSelectedIndex == i) {
                        textStyle.fontSize * itemScaleFact
                    } else {
                        textStyle.fontSize
                    }
                )
            }
        })
    }
}


@Composable
fun AmPmSelector(
    initialIsAm: Boolean = true, onValueChanged: (Boolean) -> Unit = {}
) {
    val items = listOf("AM", "PM")
    val itemHeight = 24.dp
    val scope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()
    var selectedIndex by remember { mutableStateOf(if (initialIsAm) 0 else 1) }
    val density = LocalDensity.current

    // Initial scroll position
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

                            if (abs(position - middlePosition) < with(density) { itemHeight.toPx() } / 2) {
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

            // Bottom spacer
            item {
                Spacer(modifier = Modifier.height(itemHeight))
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ScrollWheelPreview() {
    var day by remember {
        mutableStateOf(1)
    }
    var month by remember {
        mutableStateOf(1)
    }
    var year by remember {
        mutableStateOf(2023)
    }
    var lastDayInMonth by remember {
        mutableStateOf(55)
    }

    var isAm by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(
            20.dp, Alignment.CenterVertically
        ), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScrollWheel(width = 70.dp,
            itemHeight = 50.dp,
            items = (1..lastDayInMonth).toMutableList(),
            initialItem = day,
            textStyle = TextStyle(fontSize = 23.sp),
            textColor = Color.LightGray,
            selectedTextColor = Color.Black,
            onItemSelected = { i, item ->
                day = item
            })
        AmPmSelector(initialIsAm = isAm, onValueChanged = {
            isAm = it
        })

    }


}