package com.cornellappdev.uplift.ui.components.classdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.cornellappdev.uplift.data.models.UpliftClass
import com.cornellappdev.uplift.ui.components.general.ClassInfoCard
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.montserratFamily

/**
 * A component that displays an [UpliftClass]'s next sessions.
 */
@Composable
fun NextUpliftClassSessions(
    navController: NavHostController,
    nextSessions: List<UpliftClass>,
    openClass: (UpliftClass) -> Unit
) {

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
        for (nextClass: UpliftClass in nextSessions) {
            ClassInfoCard(
                thisClass = nextClass,
                navController = navController,
                openClass = openClass
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
        Spacer(Modifier.height(24.dp))
    }
}
