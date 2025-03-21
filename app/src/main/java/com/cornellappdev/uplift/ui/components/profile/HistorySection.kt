package com.cornellappdev.uplift.ui.components.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.montserratFamily

data class HistoryItem(
    val gymName: String,
    val time: String,
    val date: String,
)

@Composable
fun HistorySection(
    historyItems: List<HistoryItem>
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SectionTitleText("HISTORY")
        HistoryList(historyItems)
    }


}

@Composable
private fun HistoryList(historyItems: List<HistoryItem>) {
    Column {
        historyItems.forEachIndexed { index, historyItem ->
            HistoryItemRow(historyItem = historyItem)
            if (index != historyItems.size - 1) {
                HorizontalDivider(color = GRAY01)
            }
        }
    }
}

@Composable
private fun HistoryItemRow(
    historyItem: HistoryItem
) {
    val gymName = historyItem.gymName
    val time = historyItem.time
    val date = historyItem.date
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = gymName,
            fontFamily = montserratFamily,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
        Text(
            text = "$time · $date",
            fontFamily = montserratFamily,
            fontSize = 12.sp,
            fontWeight = FontWeight.Light,
            color = Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HistorySectionPreview() {
    val historyItems = listOf(
        HistoryItem("Morrison", "11:00 PM", "10/14/2025"),
        HistoryItem("Noyes", "1:00 PM", "10/13/2025"),
        HistoryItem("Teagle Up", "2:00 PM", "10/12/2025"),
        HistoryItem("Teagle Down", "12:00 PM", "10/11/2025"),
        HistoryItem("Helen Newman", "10:00 AM", "10/09/2025"),
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        HistorySection(
            historyItems = historyItems
        )
    }

}