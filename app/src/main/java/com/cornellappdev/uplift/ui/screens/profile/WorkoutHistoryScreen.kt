package com.cornellappdev.uplift.ui.screens.profile

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.components.general.UpliftTabRow
import com.cornellappdev.uplift.ui.components.profile.workouts.EmptyHistorySection
import com.cornellappdev.uplift.ui.components.profile.workouts.HistoryItem
import com.cornellappdev.uplift.ui.components.profile.workouts.HistoryItemRow
import com.cornellappdev.uplift.ui.viewmodels.profile.HistoryListItem
import com.cornellappdev.uplift.ui.viewmodels.profile.ProfileUiState
import com.cornellappdev.uplift.ui.viewmodels.profile.ProfileViewModel
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.GRAY04
import com.cornellappdev.uplift.util.LIGHT_GRAY
import com.cornellappdev.uplift.util.LIGHT_YELLOW
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.PRIMARY_YELLOW
import com.cornellappdev.uplift.util.montserratFamily
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun WorkoutHistoryScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val uiState = viewModel.collectUiStateValue()
    WorkoutHistoryScreenContent(uiState = uiState, onBack = onBack)
}

@Composable
fun WorkoutHistoryScreenContent(
    uiState: ProfileUiState,
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        WorkoutHistoryHeader(onBack = onBack)
        UpliftTabRow(
            tabIndex = selectedTab,
            tabs = listOf("Calendar", "List"),
            icons = listOf(R.drawable.ic_calendar_tab, R.drawable.ic_list_tab),
            onTabChange = { selectedTab = it }
        )

        if (uiState.historyItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                EmptyHistorySection()
            }
        } else {
            AnimatedContent(targetState = selectedTab, label = "historyTabContent") { tab ->
                when (tab) {
                    0 -> WorkoutHistoryCalendarView(workoutDates = uiState.workoutDates)
                    1 -> WorkoutHistoryListView(listItems = uiState.historyListItems)
                }
            }
        }
    }
}

@Composable
private fun WorkoutHistoryHeader(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(LIGHT_GRAY)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = { onBack() },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back_arrow),
                contentDescription = "Back",
                modifier = Modifier
                    .size(24.dp),
                tint = PRIMARY_BLACK
            )
        }

        Text(
            text = "History",
            fontFamily = montserratFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = PRIMARY_BLACK,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.size(24.dp))
    }
}

@Composable
private fun WorkoutHistoryListView(listItems: List<HistoryListItem>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
    ) {
        item(key = "top_spacer") {
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(
            items = listItems,
            key = { listItem ->
                when (listItem) {
                    is HistoryListItem.Header -> "header_${listItem.month}"
                    is HistoryListItem.Workout -> "workout_${listItem.item.timestamp}"
                    is HistoryListItem.SpacerItem -> "spacer_${listItem.month}"
                }
            }
        ) { listItem ->
            when (listItem) {
                is HistoryListItem.Header -> {
                    Text(
                        text = listItem.month,
                        fontFamily = montserratFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                is HistoryListItem.Workout -> {
                    Column {
                        HistoryItemRow(historyItem = listItem.item)
                        if (listItem.showDivider) {
                            HorizontalDivider(color = GRAY01, thickness = 1.dp)
                        }
                    }
                }

                is HistoryListItem.SpacerItem -> {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun WorkoutHistoryCalendarView(workoutDates: Map<LocalDate,List<HistoryItem>>) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Month Selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    currentMonth = currentMonth.minusMonths(1)
                    selectedDate = null
                }

            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back_month),
                    contentDescription = "Previous Month",
                    modifier = Modifier
                        .size(16.dp),
                    tint = PRIMARY_BLACK
                )
            }
            Text(
                text = currentMonth.format(DateTimeFormatter.ofPattern("MMM yyyy", Locale.US)),
                fontFamily = montserratFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = PRIMARY_BLACK,
                modifier = Modifier.width(100.dp),
                textAlign = TextAlign.Center
            )

            IconButton(
                onClick = {
                    currentMonth = currentMonth.plusMonths(1)
                    selectedDate = null
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_advance_month),
                    contentDescription = "Next Month",
                    modifier = Modifier
                        .size(16.dp),
                    tint = PRIMARY_BLACK
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Days of Week Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val daysOfWeek = listOf("M", "T", "W", "Th", "F", "Sa", "Su")
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.width(40.dp),
                    fontFamily = montserratFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Calendar Grid
        AnimatedContent(
            targetState = currentMonth,
            label = "calendarMonthTransition"
        ) { animatedMonth ->

            val daysInMonth = animatedMonth.lengthOfMonth()
            val firstOfMonth = animatedMonth.atDay(1)
            val firstDayOfWeek = (firstOfMonth.dayOfWeek.value + 6) % 7

            val weeks = remember(animatedMonth) {
                val list = mutableListOf<List<LocalDate?>>()
                var currentDay = 1

                while (currentDay <= daysInMonth) {
                    val week = (0..6).map { i ->
                        val dayIndex = list.size * 7 + i
                        val dayOfMonth = dayIndex - firstDayOfWeek + 1

                        if (dayOfMonth in 1..daysInMonth) {
                            animatedMonth.atDay(dayOfMonth)
                        } else null
                    }

                    list.add(week)
                    currentDay += 7 - (if (list.size == 1) firstDayOfWeek else 0)
                }

                list
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                weeks.forEach { week ->
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            week.forEachIndexed { index, date ->
                                if (date != null) {
                                    val hasWorkout = workoutDates.containsKey(date)
                                    val isToday = date == LocalDate.now()
                                    val isSelected = selectedDate == date

                                    CalendarDayCell(
                                        day = date.dayOfMonth.toString(),
                                        isToday = isToday,
                                        hasWorkout = hasWorkout,
                                        modifier = Modifier.clickable {
                                            selectedDate =
                                                if (hasWorkout && selectedDate != date) date else null
                                        }
                                    )
                                } else {
                                    Spacer(modifier = Modifier.size(40.dp))
                                }
                            }
                        }

                        // Dropdown
                        val selectedInThisWeek = week.find { it == selectedDate }

                        AnimatedVisibility(
                            visible = selectedInThisWeek != null,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            selectedInThisWeek?.let { selected ->
                                val workouts = workoutDates[selected] ?: emptyList()
                                val dayOfWeekIndex = week.indexOf(selected)

                                Column {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    workouts.forEach { workout ->
                                        WorkoutCalendarDropdown(
                                            historyItem = workout,
                                            dayOfWeekIndex = dayOfWeekIndex
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


        @Composable
private fun WorkoutCalendarDropdown(
    historyItem: HistoryItem,
    dayOfWeekIndex: Int
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val horizontalPadding = 16.dp
    val availableWidth = screenWidth - (horizontalPadding * 2)
    val cellWidth = availableWidth / 7
    
    // Calculate arrow offset to point to the center of the day cell
    val arrowXOffset = (cellWidth * dayOfWeekIndex) + (cellWidth / 2)
    val density = LocalDensity.current

    val shape = remember(arrowXOffset, density) {
        GenericShape { size, _ ->
            val arrowWidth = with(density) { 12.dp.toPx() }
            val arrowHeight = with(density) { 8.dp.toPx() }
            val cornerRadius = with(density) { 12.dp.toPx() }
            val arrowX = with(density) { arrowXOffset.toPx() }

            // Top edge with arrow
            moveTo(0f, arrowHeight + cornerRadius)
            arcTo(
                rect = Rect(0f, arrowHeight, cornerRadius * 2, arrowHeight + cornerRadius * 2),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            lineTo(arrowX - arrowWidth / 2, arrowHeight)
            lineTo(arrowX, 0f)
            lineTo(arrowX + arrowWidth / 2, arrowHeight)
            lineTo(size.width - cornerRadius, arrowHeight)
            arcTo(
                rect = Rect(size.width - cornerRadius * 2, arrowHeight, size.width, arrowHeight + cornerRadius * 2),
                startAngleDegrees = 270f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            // Right edge
            lineTo(size.width, size.height - cornerRadius)
            arcTo(
                rect = Rect(size.width - cornerRadius * 2, size.height - cornerRadius * 2, size.width, size.height),
                startAngleDegrees = 0f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            // Bottom edge
            lineTo(cornerRadius, size.height)
            arcTo(
                rect = Rect(0f, size.height - cornerRadius * 2, cornerRadius * 2, size.height),
                startAngleDegrees = 90f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            close()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = LIGHT_YELLOW, shape = shape)
            .border(width = 1.dp, color = PRIMARY_YELLOW, shape = shape)
            .padding(top = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = historyItem.gymName,
                    fontFamily = montserratFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = PRIMARY_BLACK
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = historyItem.time,
                        fontFamily = montserratFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = GRAY04
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(
                        modifier = Modifier
                            .size(2.dp)
                            .background(GRAY04, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    val shortDate = remember(historyItem.timestamp) {
                        Instant.ofEpochMilli(historyItem.timestamp)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                            .format(DateTimeFormatter.ofPattern("MMM d", Locale.US))
                    }
                    Text(
                        text = shortDate,
                        fontFamily = montserratFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = GRAY04
                    )
                }
            }
            Text(
                text = historyItem.ago,
                fontFamily = montserratFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = PRIMARY_BLACK
            )
        }
    }
}

@Composable
private fun CalendarDayCell(
    day: String,
    isToday: Boolean,
    hasWorkout: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.width(40.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    color = if (isToday) LIGHT_YELLOW else Color.Transparent,
                    shape = CircleShape
                )
                .then(
                    if (isToday) Modifier.border(1.dp, PRIMARY_YELLOW, CircleShape)
                    else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = day,
                fontFamily = montserratFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = PRIMARY_BLACK,
                textAlign = TextAlign.Center
            )
        }
        if (hasWorkout) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(color = PRIMARY_YELLOW, shape = CircleShape)
            )
        } else {
            Spacer(modifier = Modifier.size(8.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun WorkoutHistoryScreenPreview() {
    val now = System.currentTimeMillis()
    val historyItems = listOf(
        HistoryItem("Morrison", "11:00 PM", "March 29, 2024", now, "Today"),
        HistoryItem("Noyes", "1:00 PM", "March 28, 2024", now - 86400000L, "Yesterday"),
        HistoryItem("Teagle Up", "2:00 PM", "February 15, 2024", now - 4000000000L, "1 month ago"),
        HistoryItem("Helen Newman", "9:30 AM", "February 10, 2024", now - 4430000000L, "1 month ago"),
        HistoryItem("Morrison", "6:45 PM", "February 3, 2024", now - 5030000000L, "1 month ago")
    )

    val listItems = buildList {
        historyItems
            .sortedByDescending { it.timestamp }
            .groupBy {
                Instant.ofEpochMilli(it.timestamp)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.US))
            }
            .forEach { (month, items) ->
                add(HistoryListItem.Header(month))
                items.forEachIndexed { index, item ->
                    add(
                        HistoryListItem.Workout(
                            item,
                            index < items.lastIndex
                        )
                    )
                }
                add(HistoryListItem.SpacerItem(month))
            }
    }

    val workoutDates = historyItems.groupBy {
        Instant.ofEpochMilli(it.timestamp)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }

    WorkoutHistoryScreenContent(
        uiState = ProfileUiState(
            historyItems = historyItems,
            historyListItems = listItems,
            workoutDates = workoutDates
        ),
        onBack = {}
    )
}
