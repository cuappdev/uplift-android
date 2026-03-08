package com.cornellappdev.uplift.ui.screens.onboarding

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.Credential
import androidx.hilt.navigation.compose.hiltViewModel
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.components.onboarding.auth.LogInButton
import com.cornellappdev.uplift.ui.viewmodels.onboarding.LoginViewModel
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.GRAY04
import com.cornellappdev.uplift.util.LIGHT_YELLOW
import com.cornellappdev.uplift.util.montserratFamily

@Composable
fun SignInPromptScreen(
    loginViewModel: LoginViewModel = hiltViewModel(),
) {
    SignInPromptScreenContent(
        loginViewModel::onSignInWithGoogle,
        loginViewModel::onSkip
    )
}

@Composable
private fun SignInPromptScreenContent(
    onSignInWithGoogle: (credential: Credential) -> Unit,
    onSkip: () -> Unit
) {
    Canvas(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
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
        Spacer(modifier = Modifier.weight(0.1f))

        Image(
            painter = painterResource(id = R.drawable.ic_main_logo),
            contentDescription = "Uplift logo",
            modifier = Modifier
                .height(130.dp)
                .width(115.dp)
        )
        Spacer(modifier = Modifier.weight(0.07f))

        Text(
            text = "Find what uplifts you.",
            fontSize = 24.sp,
            fontFamily = montserratFamily,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.weight(0.1f))

        Text(
            "Log in to:",
            fontSize = 16.sp,
            fontFamily = montserratFamily,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.weight(0.03f))

        UpliftUsesCardList()

        Spacer(modifier = Modifier.weight(0.16f))

        LogInButton(onRequestResult = onSignInWithGoogle)

        Spacer(modifier = Modifier.weight(0.02f))

        SkipButton(onClick = onSkip)

        Spacer(modifier = Modifier.weight(0.06f))
    }
}

@Composable
private fun SkipButton(onClick: () -> Unit) {
    TextButton(
        onClick = onClick
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
        UpliftUsesCard(
            R.drawable.goal,
            "Create fitness goals"
        )
        UpliftUsesCard(
            R.drawable.gym_simple,
            "Track fitness progress"
        )
        UpliftUsesCard(
            R.drawable.capacities,
            "View workout history"
        )
    }
}

@Composable
private fun UpliftUsesCard(
    @DrawableRes imageId: Int,
    text: String

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
            Image(painter = painterResource(imageId), contentDescription = text)
            Text(
                text,
                fontSize = 16.sp,
                fontFamily = montserratFamily,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SignInPromptScreenPreview() {
    SignInPromptScreenContent({}, {})
}
