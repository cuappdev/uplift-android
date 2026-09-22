package com.cornellappdev.uplift.ui.screens.profile

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.Credential
import androidx.hilt.navigation.compose.hiltViewModel
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.components.onboarding.auth.LogInButton
import com.cornellappdev.uplift.ui.viewmodels.onboarding.LoginViewModel
import com.cornellappdev.uplift.util.LIGHT_YELLOW
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.montserratFamily

@Composable
fun GuestProfileScreen(
    loginViewModel: LoginViewModel = hiltViewModel(),
) {
    GuestProfileScreenContent(loginViewModel::onSignInWithGoogle)
}

@Composable
private fun GuestProfileScreenContent(onSignIn: (Credential) -> Unit) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .clipToBounds()
    ) {
        val headerScale = (maxHeight.value / 769f).coerceIn(0.65f, 1.2f)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(375.dp * headerScale)
                    .drawBehind {
                        drawCircle(
                            color = LIGHT_YELLOW,
                            radius = 353.5.dp.toPx() * headerScale,
                            center = Offset(size.width * 101.5f / 393f, 4.5.dp.toPx() * headerScale)
                        )
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(136.dp * headerScale))
                Image(
                    painter = painterResource(R.drawable.ic_main_logo),
                    contentDescription = "Uplift logo",
                    modifier = Modifier
                        .width(207.dp * headerScale)
                        .height(183.dp * headerScale)
                )
            }
            Spacer(Modifier.height(24.dp))
            Text(
                text = "Create your Uplift profile.",
                modifier = Modifier.padding(horizontal = 16.dp),
                fontFamily = montserratFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                lineHeight = 30.sp,
                color = PRIMARY_BLACK,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(24.dp))
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                GuestProfileBenefit(R.drawable.guest_profile_goal, "Create fitness goals")
                GuestProfileBenefit(R.drawable.gym_simple, "Track fitness progress")
                GuestProfileBenefit(R.drawable.history, "View workout history")
            }
            Spacer(Modifier.height(48.dp))
            LogInButton(
                onRequestResult = onSignIn,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun GuestProfileBenefit(@DrawableRes icon: Int, text: String) {
    Row(
        modifier = Modifier
            .width(240.dp)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = text,
            fontFamily = montserratFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 16.sp,
            color = PRIMARY_BLACK.copy(alpha = 0.9f)
        )
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 769)
@Composable
private fun GuestProfilePreview() {
    GuestProfileScreenContent {}
}
