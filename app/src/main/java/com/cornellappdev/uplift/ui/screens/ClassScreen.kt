package com.cornellappdev.uplift.ui.screens

import android.icu.util.Calendar
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.cornellappdev.uplift.ui.components.ClassInfoCard
import com.cornellappdev.uplift.ui.components.general.UpliftTopBar
import com.cornellappdev.uplift.ui.components.home.BriefClassInfoCard
import com.cornellappdev.uplift.ui.viewmodels.ClassDetailViewModel
import com.cornellappdev.uplift.util.GRAY04
import com.cornellappdev.uplift.util.exampleClassMusclePump1
import com.cornellappdev.uplift.util.exampleClassMusclePump2
import com.cornellappdev.uplift.util.montserratFamily
import com.himanshoe.kalendar.Kalendar
import com.himanshoe.kalendar.model.KalendarType

class dateInstance(var date: Int, var day1:String) {
}
fun calendarDayOfWeekToString1(calendar: Calendar): String {
    val dayString = when (calendar.get(java.util.Calendar.DAY_OF_WEEK)) {
        java.util.Calendar.MONDAY -> "M"
        java.util.Calendar.TUESDAY -> "T"
        java.util.Calendar.WEDNESDAY -> "W"
        java.util.Calendar.THURSDAY -> "Th"
        java.util.Calendar.FRIDAY -> "F"
        java.util.Calendar.SATURDAY -> "Sa"
        java.util.Calendar.SUNDAY -> "Su"
        else -> "M"
    }

    return dayString
}
@Composable
fun Calendar(calendar:Calendar){
    val c: Calendar = Calendar.getInstance()
    val date = calendar.get(java.util.Calendar.DAY_OF_MONTH)
    val day= calendarDayOfWeekToString1(c)
    val dayList= listOf("M","T","W","Th","F","Sa","Su")
}
@Composable
fun ClassScreen(
                classDetailViewModel: ClassDetailViewModel = viewModel()
) {
    var navController = rememberNavController()
    val clasesList = listOf(
        exampleClassMusclePump1,
        exampleClassMusclePump2,
        exampleClassMusclePump1,
        exampleClassMusclePump2,
        exampleClassMusclePump1,
        exampleClassMusclePump2
    )
    val classesScrollState = rememberLazyListState()

    Column {
    UpliftTopBar(showIcon = true, title = "Classes")
        Text(
            textAlign = TextAlign.Center,
            text = "TODAY",
            fontFamily = montserratFamily,
            fontSize = 20.sp,
            fontWeight = FontWeight(700),
            color = GRAY04,
            modifier = Modifier.padding(vertical = 14.dp)
        )
        LazyColumn(
            state = classesScrollState,
            modifier = Modifier.fillMaxWidth()
                .padding(vertical=8.dp,horizontal=8.dp)
        ) {
            items(items = clasesList) { clasesList ->
                ClassInfoCard(
                    thisClass = clasesList,
                )
            }
        }
    }
}