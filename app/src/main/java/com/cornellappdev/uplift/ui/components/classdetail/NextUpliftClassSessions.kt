package com.cornellappdev.uplift.ui.components.classdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.cornellappdev.uplift.networking.ApiResponse
import com.cornellappdev.uplift.networking.UpliftApiRepository
import com.cornellappdev.uplift.ui.components.ClassInfoCard
import com.cornellappdev.uplift.ui.viewmodels.ClassDetailViewModel
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.montserratFamily
import java.util.*

/**
 * A component that displays an [UpliftClass]'s next sessions.
 */
@Composable
fun NextUpliftClassSessions(
    upliftClass: UpliftClass?,
    navController: NavHostController,
    classDetailViewModel: ClassDetailViewModel
) {
    val classesState = UpliftApiRepository.upliftClassesFlow.collectAsState()
    val classes = (when (classesState.value) {
        is ApiResponse.Success ->
            (classesState.value as ApiResponse.Success<List<UpliftClass>>).data.filter {
                it.name == upliftClass?.name && it.date > GregorianCalendar()
            }
        else -> listOf()
    })

    // Next Sessions
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(24.dp))
        Text(
            text = "NEXT SESSIONS",
            fontWeight = FontWeight(700),
            fontSize = 16.sp,
            lineHeight = 19.5.sp,
            textAlign = TextAlign.Center,
            color = PRIMARY_BLACK,
            fontFamily = montserratFamily
        )
        Spacer(Modifier.height(24.dp))
        for (nextClass: UpliftClass in classes) {
            ClassInfoCard(
                thisClass = nextClass,
                navController = navController,
                classDetailViewModel = classDetailViewModel
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
        Spacer(Modifier.height(36.dp))
    }
}