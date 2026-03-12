package com.cornellappdev.uplift.ui.components.profile.workouts

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.components.profile.SectionTitleText
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.montserratFamily
import com.cornellappdev.uplift.util.timeAgoString
import java.util.Calendar

data class HistoryItem(
    val gymName: String,
    val time: String,
    val date: String,
    val timestamp: Long
)

@Composable
fun HistorySection(
    historyItems: List<HistoryItem>,
    onClick : () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        SectionTitleText("My Workout History", onClick)
        Spacer(modifier = Modifier.height(12.dp))
        if (historyItems.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                HistoryList(historyItems)
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                EmptyHistorySection()
            }
        }
    }


}

@Composable
private fun HistoryList(historyItems: List<HistoryItem>) {
    Column {
        historyItems.take(5).forEachIndexed { index, historyItem ->
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
    val calendar = Calendar.getInstance().apply {
        timeInMillis = historyItem.timestamp
    }
    val ago = calendar.timeAgoString()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(){
            Text(
                text = gymName,
                fontFamily = montserratFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Text(
                text = "$date · $time",
                fontFamily = montserratFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                color = Color.Gray
            )
        }
        Text(
            text = ago,
            fontFamily = montserratFamily,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}

@Composable
private fun EmptyHistorySection(){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_dufflebag),
            contentDescription = null,
            modifier = Modifier
                .width(64.99967.dp)
                .height(50.8181.dp)

        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "No workouts yet.",
            fontFamily = montserratFamily,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = "Head to a gym and check in!",
            fontFamily = montserratFamily,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HistorySectionPreview() {
    val now = System.currentTimeMillis()
    val historyItems = listOf(
        HistoryItem("Morrison", "11:00 PM",  "March 29, 2024", now - (1 * 24 * 60 * 60 * 1000) ),
        HistoryItem("Noyes", "1:00 PM", "March 29, 2024", now - (3 * 24 * 60 * 60 * 1000)),
        HistoryItem("Teagle Up", "2:00 PM",  "March 29, 2024", now - (7 * 24 * 60 * 60 * 1000)),
        HistoryItem("Teagle Down", "12:00 PM",  "March 29, 2024", now - (15 * 24 * 60 * 60 * 1000)),
        HistoryItem("Helen Newman", "10:00 AM",  "March 29, 2024", now),
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        HistorySection(
            historyItems = historyItems,
            onClick = {}
        )
    }

}