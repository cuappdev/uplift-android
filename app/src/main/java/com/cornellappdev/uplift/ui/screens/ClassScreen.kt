package com.cornellappdev.uplift.ui.screens

import android.icu.util.Calendar
import android.transition.Visibility
import android.widget.Button
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.SemanticsActions.OnClick
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.cornellappdev.uplift.R
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
                text = "TODAY",
                fontFamily = montserratFamily,
                fontSize = 20.sp,
                fontWeight = FontWeight(700),
                color = PRIMARY_BLACK,
                modifier = Modifier
                    .padding(vertical = 14.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,

                )
            LazyColumn(
                state = classesScrollState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top=8.dp,bottom=20.dp)
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
                .width(164.dp)
                .height(73.dp)
                .padding(bottom=20.dp)
                .align(BottomCenter),
            shape=RoundedCornerShape(12.dp),
            colors=ButtonDefaults.buttonColors(Color.White)
        ) {
            Text(
                text = "APPLY FILTER",
                fontFamily = montserratFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight(700),
                color = PRIMARY_BLACK,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,

                )
        }
    }
}

