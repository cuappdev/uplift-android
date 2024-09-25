package com.cornellappdev.uplift.ui.screens

import android.widget.ImageButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.GRAY02
import com.cornellappdev.uplift.util.GRAY03
import com.cornellappdev.uplift.util.PRIMARY_YELLOW
import com.cornellappdev.uplift.util.montserratFamily
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * The profile creation page during the Uplift onboarding process.
 */
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProfileCreationScreen() {
    val systemUiController: SystemUiController = rememberSystemUiController()
    val checkboxColors: CheckboxColors =
        CheckboxDefaults.colors(checkedColor = PRIMARY_YELLOW, checkmarkColor = Color.Black)

    systemUiController.isStatusBarVisible = false
    systemUiController.isNavigationBarVisible = false // Navigation bar
    systemUiController.isSystemBarsVisible = false

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = "Complete your profile.",
                    modifier = Modifier.padding(vertical = 4.dp),
                    fontSize = 24.sp,
                    fontFamily = montserratFamily,
                    fontWeight = FontWeight.Bold
                )
            },
            modifier = Modifier.shadow(elevation = 20.dp, ambientColor = GRAY01)
        )
    }) { innerPadding ->
        val checkedState = remember { mutableStateOf(false) }
        val checkedState2 = remember { mutableStateOf(false) }
        val checkedState3 = remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .padding((innerPadding))
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(46.dp))
            //TODO - this Box placeholder will be replaced with a component to choose photo
            Box(
                modifier = Modifier
                    .size(155.dp)
                    .shadow(
                        elevation = 20.dp,
                        shape = CircleShape,
                        ambientColor = GRAY01,
                        spotColor = GRAY03
                    )
                    .background(color = GRAY01, shape = CircleShape)
                    .border(width = 8.dp, color = Color.White, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.camera),
                    contentDescription = "",
                    modifier = Modifier.offset(y = 5.dp)
                )
            }
            Text(
                text = "First name Last name",
                modifier = Modifier.padding(vertical = 24.dp),
                fontSize = 24.sp,
                fontFamily = montserratFamily,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(25.dp))



            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = checkedState.value,
                        onCheckedChange = { checkedState.value = it },
                        colors = checkboxColors
                    )
                    Text(
                        text = "I agree with EULA terms and agreements",
                        fontSize = 12.sp,
                        fontFamily = montserratFamily,
                        fontWeight = FontWeight.Normal
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = checkedState2.value,
                        onCheckedChange = { checkedState2.value = it },
                        colors = checkboxColors
                    )
                    Text(
                        text = "I allow Uplift to access data on my gym usage",
                        fontSize = 12.sp,
                        fontFamily = montserratFamily,
                        fontWeight = FontWeight.Normal
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = checkedState3.value,
                        onCheckedChange = { checkedState3.value = it },
                        colors = checkboxColors
                    )
                    Text(
                        text = "I allow Uplift to access my location",
                        fontSize = 12.sp,
                        fontFamily = montserratFamily,
                        fontWeight = FontWeight.Normal
                    )
                }

            }

            if (checkedState.value && checkedState2.value && checkedState3.value) {
                Spacer(modifier = Modifier.height(155.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 25.dp)
                ) {
                    Text(
                        text = "Are you ready to",
                        fontSize = 16.sp,
                        fontFamily = montserratFamily,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Image(
                        painter = painterResource(R.drawable.ic_main_logo),
                        contentDescription = "",
                        modifier = Modifier.size(width = 26.dp, height = 23.dp)
                    )

                    Image(
                        painter = painterResource(R.drawable.lift_logo),
                        contentDescription = "",
                    )
                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "?",
                        fontSize = 16.sp,
                        fontFamily = montserratFamily,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(205.dp))
            }
            Button(
                {},
                enabled = checkedState.value && checkedState2.value && checkedState3.value,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = PRIMARY_YELLOW,
                    disabledBackgroundColor = GRAY02,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(38.dp),
                modifier = Modifier.size(height = 44.dp, width = 144.dp)
            ) {
                Text(
                    text = if (checkedState.value && checkedState2.value && checkedState3.value) "Get started" else "Next",
                    fontSize = 16.sp,
                    fontFamily = montserratFamily,
                    fontWeight = FontWeight.Bold
                )
            }

        }
    }
}