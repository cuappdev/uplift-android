package com.cornellappdev.uplift.ui.screens.onboarding

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.viewmodels.onboarding.LoginViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.GRAY04
import com.cornellappdev.uplift.util.LIGHT_YELLOW
import com.cornellappdev.uplift.util.PRIMARY_YELLOW
import com.cornellappdev.uplift.util.montserratFamily
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun SignInPromptScreen(
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val systemUiController: SystemUiController = rememberSystemUiController()

    systemUiController.isStatusBarVisible = false
    systemUiController.isNavigationBarVisible = false // Navigation bar
    systemUiController.isSystemBarsVisible = false

    val resultLauncher = loginViewModel.makeSignInLauncher()

    Box() {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = LIGHT_YELLOW,
                radius = 244.dp.toPx(),
                center = Offset(x = 142.dp.toPx(), y = (-35).dp.toPx())
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(88.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_main_logo),
                contentDescription = "Uplift logo",
                modifier = Modifier
                    .height(130.dp)
                    .width(115.dp)
            )
            Spacer(modifier = Modifier.height(60.dp))

            Text(
                text = "Find what uplifts you.",
                fontSize = 24.sp,
                fontFamily = montserratFamily,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(80.dp))

            Text(
                "Log in to:",
                fontSize = 16.sp,
                fontFamily = montserratFamily,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            UpliftUsesCardList()

            Spacer(modifier = Modifier.height(125.dp))
            SignInButton(
                onClick = {
                    resultLauncher.launch(loginViewModel.getSignInClient().signInIntent)

                },
                nextOnClick = {}
            )
        }
    }
}

@Composable
private fun SignInButton(onClick: () -> Unit, nextOnClick: () -> Unit) {
    Button(
        onClick = onClick,
        elevation = ButtonDefaults.elevation(5.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = PRIMARY_YELLOW)
    ) {
        //TODO - this should trigger the sign in call to backend; for now it moves on to profile
        // creation as if the sign in was successful
        Text(
            "Log in",
            fontSize = 16.sp,
            fontFamily = montserratFamily,
            fontWeight = FontWeight.Bold
        )
    }

    Spacer(modifier = Modifier.height(12.dp))
    TextButton(
        onClick = nextOnClick
    ) {
        Text(
            "Skip",
            fontSize = 14.sp,
            fontFamily = montserratFamily,
            fontWeight = FontWeight.Normal,
            color = GRAY04
        )
    }
}

@Composable
private fun UpliftUsesCardList() {
    Column(
        modifier = Modifier.wrapContentHeight(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedCard(
            modifier = Modifier
                .wrapContentHeight()
                .width(240.dp)
                .shadow(
                    elevation = 20.dp,
                    ambientColor = GRAY01,
                    spotColor = GRAY01,
                    shape = RoundedCornerShape(8.dp)
                ), colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(12.dp)
            ) {
                Image(painter = painterResource(R.drawable.goal), contentDescription = "")
                Text(
                    "Create fitness goals",
                    fontSize = 16.sp,
                    fontFamily = montserratFamily,
                    fontWeight = FontWeight.Normal
                )
            }
        }
        ElevatedCard(
            modifier = Modifier
                .wrapContentHeight()
                .width(240.dp)
                .shadow(
                    elevation = 20.dp,
                    ambientColor = GRAY01,
                    spotColor = GRAY01,
                    shape = RoundedCornerShape(8.dp)
                ), colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(12.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.gym_simple),
                    contentDescription = ""
                )
                Text(
                    "Track fitness progress",
                    fontSize = 16.sp,
                    fontFamily = montserratFamily,
                    fontWeight = FontWeight.Normal
                )
            }
        }
        ElevatedCard(
            modifier = Modifier
                .wrapContentHeight()
                .width(240.dp)
                .shadow(
                    elevation = 20.dp,
                    ambientColor = GRAY01,
                    spotColor = GRAY01,
                    shape = RoundedCornerShape(8.dp)
                ), colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(12.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.history),
                    modifier = Modifier.size(22.dp),
                    contentDescription = ""
                )
                Text(
                    "View workout history",
                    fontSize = 16.sp,
                    fontFamily = montserratFamily,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}