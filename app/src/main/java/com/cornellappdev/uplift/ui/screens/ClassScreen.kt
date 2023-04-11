package com.cornellappdev.uplift.ui.screens

import android.icu.util.Calendar
import android.widget.Button
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.SemanticsActions.OnClick
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
import com.cornellappdev.uplift.util.*
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
    Box {
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 8.dp)
            ) {
                items(items = clasesList) { clasesList ->
                    ClassInfoCard(
                        thisClass = clasesList,
                    )
                }
            }
        }
        Button(onClick = { /*TODO*/ },
            modifier= Modifier
                .align(BottomCenter)
                .size(64.dp),
            shape=RoundedCornerShape(12.dp)
        ) {
            Text(text="Apply Filter")
        }
    }
}
@Composable
fun filteringScreen(){
    Column() {
        var newman by remember { mutableStateOf(false) }
        var noyes = remember { mutableStateOf(false) }
        var teagle by remember { mutableStateOf(false) }
        var appel by remember { mutableStateOf(false) }
        TopAppBar(
            backgroundColor = GRAY01
        )
        {

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Reset",
                    modifier = Modifier.weight(1F)
                        .clickable { }
                        .padding(start = 16.dp),
                    fontSize = 14.sp,
                    fontFamily = montserratFamily,
                    fontWeight = FontWeight(500),
                    color = PRIMARY_BLACK
                )
                Text(
                    text = "Reset",
                    modifier = Modifier.weight(1F),
                    fontSize = 14.sp,
                    fontFamily = montserratFamily,
                    fontWeight = FontWeight(700),
                    color = PRIMARY_BLACK,

                    )
                Text(
                    text = "Done",
                    modifier = Modifier.weight(1F)
                        .clickable { }
                        .padding(end = 16.dp),
                    fontSize = 14.sp,
                    fontFamily = montserratFamily,
                    fontWeight = FontWeight(500),
                    color = PRIMARY_BLACK
                )
            }
        }


        Text(
            text = "Fitness Centers",
            modifier = Modifier
                .padding(start = 16.dp),
            fontSize = 12.sp,
            fontFamily = montserratFamily,
            fontWeight = FontWeight(700),
            color = GRAY04
        )
        Row() {
            Text(
                text = "Noyes",
                modifier = Modifier
                    .padding(start = 16.dp)
                    .clickable {
                    if(noyes.value){

                    }
                    },
                fontSize = 14.sp,
                fontFamily = montserratFamily,
                fontWeight = FontWeight(300),
                color = PRIMARY_BLACK
            )
            Divider(color = GRAY01, thickness = 1.dp)
            Text(
                text = "Helen Newman",
                modifier = Modifier
                    .padding(start = 16.dp)
                    .clickable {
                    },
                fontSize = 14.sp,
                fontFamily = montserratFamily,
                fontWeight = FontWeight(300),
                color = PRIMARY_BLACK
            )
            Divider(color = GRAY01, thickness = 1.dp)
            Text(
                text = "Teagle",
                modifier = Modifier
                    .padding(start = 16.dp)
                    .clickable {
                    },
                fontSize = 14.sp,
                fontFamily = montserratFamily,
                fontWeight = FontWeight(300),
                color = PRIMARY_BLACK
            )
            Divider(color = GRAY01, thickness = 1.dp)
            Text(
                text = "Appel",
                modifier = Modifier
                    .padding(start = 16.dp)
                    .clickable {
                    },
                fontSize = 14.sp,
                fontFamily = montserratFamily,
                fontWeight = FontWeight(300),
                color = PRIMARY_BLACK
            )
        }
    }
}