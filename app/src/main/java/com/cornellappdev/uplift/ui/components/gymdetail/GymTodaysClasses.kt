package com.cornellappdev.uplift.ui.components.gymdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.cornellappdev.uplift.models.UpliftClass
import com.cornellappdev.uplift.models.UpliftGym
import com.cornellappdev.uplift.networking.ApiResponse
import com.cornellappdev.uplift.networking.UpliftApiRepository
import com.cornellappdev.uplift.ui.components.ClassInfoCard
import com.cornellappdev.uplift.ui.viewmodels.ClassDetailViewModel
import com.cornellappdev.uplift.util.getSystemTime
import com.cornellappdev.uplift.util.montserratFamily
import com.cornellappdev.uplift.util.sameDayAs
import java.util.*

/**
 * A vertical list of classes that a gym offers today. Refers to [ClassInfoCard] to create the class
 * cards.
 */
@Composable
fun GymTodaysClasses(
    gym: UpliftGym, classDetailViewModel: ClassDetailViewModel, navController: NavHostController
) {
    // Gets Today's Classes from [UpliftApiRepository].
    val classesState = UpliftApiRepository.upliftClassesFlow.collectAsState()
    val classes = (when (classesState.value) {
        is ApiResponse.Success -> (classesState.value as ApiResponse.Success<List<UpliftClass>>).data
        else -> listOf()
    }).filter {
        it.gymId == gym.id
            && it.date.sameDayAs(GregorianCalendar()) && it.time.end.compareTo(getSystemTime()) >= 0
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "TODAY'S CLASSES",
            fontFamily = montserratFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight(700),
            lineHeight = 19.5.sp,
            modifier = Modifier.padding(top = 24.dp, bottom = 24.dp),
            textAlign = TextAlign.Center
        )
        for (aClass in classes) {
            ClassInfoCard(
                thisClass = aClass,
                classDetailViewModel = classDetailViewModel,
                navController = navController
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
        if (classes.isEmpty()) {
            Text(
                text = "We are done for today.\nPlease check again tomorrow!",
                fontFamily = montserratFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight(300),
                lineHeight = 19.5.sp,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(36.dp))
    }
}