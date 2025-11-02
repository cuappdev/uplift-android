package com.cornellappdev.uplift.ui.components.capacityreminder

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.components.general.UpliftButton
import com.cornellappdev.uplift.util.GRAY03
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.montserratFamily

@Composable
fun CapacityReminderTutorial(
    onAccept: () -> Unit,
    onDismiss: () -> Unit,
) {
    val offsetY = remember { Animatable(initialValue = 150f) }
    LaunchedEffect(Unit) {
        offsetY.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 1000)
        )
    }
    Column(
        modifier = Modifier
            .width(300.dp)
            .clip(RoundedCornerShape(20.dp))
    ) {
        Box(modifier = Modifier
            .height(175.dp)
            .clipToBounds()) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .scale(4.7f, 1.5f)
                    .align(Alignment.Center)
                    .background(
                        brush = Brush.radialGradient(
                            colorStops = arrayOf(
                                0.0f to Color(0xFFFFF5B2),
                                0.3173f to Color(0xFFFFD900),
                                0.4712f to Color(0xFFFFB24E),
                                0.6202f to Color(0xFFFFC766),
                                0.7933f to Color(0xFFC84600),
                                0.9471f to Color(0xFF341600)
                            ),
                            center = Offset(405f, 180f), // normalized to pixels
                        )
                    )
            )
            Image(
                painter = painterResource(R.drawable.capacityloading),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
            Image(
                painter = painterResource(R.drawable.ic_logo_sun_var),
                contentDescription = "Uplift logo",
                modifier = Modifier
                    .width(200.dp)
                    .align(Alignment.Center)
                    .offset(y = Dp(offsetY.value)),
                contentScale = ContentScale.FillWidth
            )
            IconButton(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = onDismiss,
                content = {
                    Icon(
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = "exit",
                    )
                }
            )
        }
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(horizontal = 25.dp, vertical = 20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Introducing a new feature:",
                fontFamily = montserratFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = PRIMARY_BLACK
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Capacity Reminders!",
                fontFamily = montserratFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = PRIMARY_BLACK
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Get real-time alerts for whenever your favorite gyms are free!",
                fontFamily = montserratFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = PRIMARY_BLACK
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Can be found under gym availabilities overview on the home page",
                fontFamily = montserratFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center,
                color = PRIMARY_BLACK
            )
            Spacer(modifier = Modifier.height(12.dp))
            Image(
                painter = painterResource(R.drawable.reminders_tutorial_diagram),
                contentDescription = "Image of area to click for capacity reminders",
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(24.dp))
            UpliftButton(
                text = "Set up now",
                containerColor = PRIMARY_BLACK,
                contentColor = Color.White,
                modifier = Modifier.fillMaxWidth(),
                onClick = onAccept)
            Spacer(modifier = Modifier.height(8.dp))
            UpliftButton(
                text = "Maybe later",
                containerColor = Color.White,
                contentColor = GRAY03,
                modifier = Modifier.fillMaxWidth(),
                elevation = 0.dp,
                onClick = onDismiss)
        }
    }
}

@Preview
@Composable
fun CapacityReminderTutorialPreview() {
    CapacityReminderTutorial({},{})
}