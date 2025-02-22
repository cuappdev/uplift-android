package com.cornellappdev.uplift.ui.screens.onboarding

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.GRAY02
import com.cornellappdev.uplift.util.GRAY03
import com.cornellappdev.uplift.util.PRIMARY_YELLOW
import com.cornellappdev.uplift.util.montserratFamily
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * The profile creation page during the Uplift onboarding process.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileCreationScreen(
    navController: NavHostController,
) {
    val systemUiController: SystemUiController = rememberSystemUiController()
    val checkboxColors: CheckboxColors =
        CheckboxDefaults.colors(
            checkedColor = PRIMARY_YELLOW,
            checkmarkColor = Color.Black,
            uncheckedColor = GRAY03
        )

    systemUiController.isStatusBarVisible = false
    systemUiController.isNavigationBarVisible = false // Navigation bar
    systemUiController.isSystemBarsVisible = false

    Scaffold(
        containerColor = Color.White,
        topBar = {
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
                modifier = Modifier
                    .shadow(elevation = 20.dp, ambientColor = GRAY01)
                    .statusBarsPadding()

            )
        }) { innerPadding ->
        val checkedState = remember { mutableStateOf(false) }
        val checkedState2 = remember { mutableStateOf(false) }
        val checkedState3 = remember { mutableStateOf(false) }
        val allchecked = checkedState.value && checkedState2.value && checkedState3.value

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
                InfoCheckboxRow(
                    checkedState = checkedState,
                    checkboxColors = checkboxColors,
                    message = "I agree with EULA terms and agreements"
                )

                InfoCheckboxRow(
                    checkedState = checkedState2,
                    checkboxColors = checkboxColors,
                    message = "I allow Uplift to access data on my gym usage"
                )

                InfoCheckboxRow(
                    checkedState = checkedState3,
                    checkboxColors = checkboxColors,
                    message = "I allow Uplift to access my location"
                )

            }


            Spacer(modifier = Modifier.height(155.dp))

            val animatedOpacity: Float by animateFloatAsState(if (allchecked) 1f else 0f)
            val opacityModifier: Modifier = Modifier.alpha(animatedOpacity)
            ReadyToUplift(opacityModifier)

            Button(
                { navController.navigate(route = "home") },
                enabled = allchecked,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = PRIMARY_YELLOW,
                    disabledBackgroundColor = GRAY02,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(38.dp),
                modifier = Modifier.size(height = 44.dp, width = 144.dp)
            ) {

                Text(
                    text = "Next",
                    fontSize = 16.sp,
                    fontFamily = montserratFamily,
                    fontWeight = FontWeight.Bold
                )

            }

        }
    }
}

@Composable
private fun ReadyToUplift(
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(bottom = 25.dp)
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
}

@Composable
private fun InfoCheckboxRow(
    checkedState: MutableState<Boolean>,
    checkboxColors: CheckboxColors,
    message: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = checkedState.value,
            onCheckedChange = { checkedState.value = it },
            colors = checkboxColors
        )
        Text(
            text = message,
            fontSize = 12.sp,
            fontFamily = montserratFamily,
            fontWeight = FontWeight.Normal
        )
    }
}