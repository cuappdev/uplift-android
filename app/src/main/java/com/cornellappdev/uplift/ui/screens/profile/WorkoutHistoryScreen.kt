package com.cornellappdev.uplift.ui.screens.profile

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.components.general.UpliftTabRow
import com.cornellappdev.uplift.ui.components.profile.workouts.EmptyHistorySection
import com.cornellappdev.uplift.ui.components.profile.workouts.HistoryItem
import com.cornellappdev.uplift.ui.components.profile.workouts.HistoryItemRow
import com.cornellappdev.uplift.ui.viewmodels.profile.ProfileUiState
import com.cornellappdev.uplift.ui.viewmodels.profile.ProfileViewModel
import com.cornellappdev.uplift.util.GRAY01
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
    val uiState by viewModel.uiStateFlow.collectAsState()
    WorkoutHistoryScreenContent(uiState = uiState, onBack = onBack)
}

@Composable
fun WorkoutHistoryScreenContent(
    uiState: ProfileUiState,
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }

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
            when (selectedTab) {
                0 -> WorkoutHistoryCalendarView(historyItems = uiState.historyItems)
                1 -> WorkoutHistoryListView(historyItems = uiState.historyItems)
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
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_back_arrow),
            contentDescription = "Back",
            modifier = Modifier
                .size(24.dp)
                .clickable { onBack() },
            tint = PRIMARY_BLACK
        )
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
private fun WorkoutHistoryListView(historyItems: List<HistoryItem>) {
    val groupedItems = remember(historyItems) {
        historyItems.groupBy {
            val date = Instant.ofEpochMilli(it.timestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            date.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.US))
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }

        groupedItems.forEach { (month, items) ->
            item {
                Text(
                    text = month,
                    fontFamily = montserratFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            itemsIndexed(items) { index, item ->
                HistoryItemRow(historyItem = item)
                if (index < items.size - 1) {
                    HorizontalDivider(color = GRAY01, thickness = 1.dp)
                }
            }
            item { Spacer(modifier = Modifier.height(24.dp)) }
        }

    }
}

@Composable
private fun WorkoutHistoryCalendarView(historyItems: List<HistoryItem>) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val workoutDates = remember(historyItems) {
        historyItems.map {
            Instant.ofEpochMilli(it.timestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }.toSet()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Month Selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back_month),
                contentDescription = "Previous Month",
                modifier = Modifier
                    .size(16.dp)
                    .clickable { currentMonth = currentMonth.minusMonths(1) },
                tint = PRIMARY_BLACK
            )
            Spacer(modifier = Modifier.width(24.dp))
            Text(
                text = currentMonth.format(DateTimeFormatter.ofPattern("MMM yyyy", Locale.US)),
                fontFamily = montserratFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = PRIMARY_BLACK,
                modifier = Modifier.width(100.dp),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(24.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_advance_month),
                contentDescription = "Next Month",
                modifier = Modifier
                    .size(16.dp)
                    .clickable { currentMonth = currentMonth.plusMonths(1) },
                tint = PRIMARY_BLACK
            )
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
        val daysInMonth = currentMonth.lengthOfMonth()
        val firstOfMonth = currentMonth.atDay(1)
        val firstDayOfWeek = (firstOfMonth.dayOfWeek.value + 6) % 7
        
        val totalCells = firstDayOfWeek + daysInMonth
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            items(totalCells) { index ->
                val dayOfMonth = index - firstDayOfWeek + 1
                if (dayOfMonth in 1..daysInMonth) {
                    val date = currentMonth.atDay(dayOfMonth)
                    val hasWorkout = workoutDates.contains(date)
                    val isToday = date == LocalDate.now()
                    
                    CalendarDayCell(
                        day = dayOfMonth.toString(),
                        isToday = isToday,
                        hasWorkout = hasWorkout
                    )
                } else {
                    Spacer(modifier = Modifier.size(40.dp))
                }
            }
        }
    }
}

@Composable
private fun CalendarDayCell(
    day: String,
    isToday: Boolean,
    hasWorkout: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.width(40.dp)
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
        HistoryItem("Morrison", "6:45 PM", "February 3, 2024", now - 5030000000L, "1 month ago"),
        HistoryItem("Noyes", "4:15 PM", "January 7, 2024", now - 8000000000L, "2 months ago")
    )

    WorkoutHistoryScreenContent(
        uiState = ProfileUiState(historyItems = historyItems),
        onBack = {}
    )
}
