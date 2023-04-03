package com.cornellappdev.uplift.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.cornellappdev.uplift.models.Gym
import com.cornellappdev.uplift.ui.viewmodels.ClassDetailViewModel
import com.cornellappdev.uplift.util.montserratFamily

/**
 * A vertical list of classes that a gym offers today. Refers to [ClassInfoCard] to create the class
 * cards.
 */
@Composable
fun GymTodaysClasses(
    gym: Gym,
    classDetailViewModel: ClassDetailViewModel,
    navController: NavHostController
) {
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
        for (aClass in gym.classesToday) {
            ClassInfoCard(
                thisClass = aClass,
                classDetailViewModel = classDetailViewModel,
                navController = navController
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
        Spacer(modifier = Modifier.height(36.dp))
    }
}