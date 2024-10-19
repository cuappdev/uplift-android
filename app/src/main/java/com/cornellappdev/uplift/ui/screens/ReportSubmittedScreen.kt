package com.cornellappdev.uplift.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.components.general.UpliftTopBarWithBack
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.GRAY04
import com.cornellappdev.uplift.util.GRAY05
import com.cornellappdev.uplift.util.montserratFamily

/**
 * Screen displayed after a user submits a report.
 */
@Composable
fun ReportSubmittedScreen(
    onSubmitAnother: () -> Unit = {},
    onReturnHome: () -> Unit = {}
) {
    Scaffold(topBar = {
        UpliftTopBarWithBack(
            title = "Report an issue",
            withBack = false
        )
    }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color.White),
            verticalArrangement = Arrangement.spacedBy(
                60.dp,
                Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(26.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_check_circle),
                    contentDescription = "Check Circle",
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Thank you for your input!",
                        fontFamily = montserratFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                    )
                    Text(
                        text = "Your report has been submitted.",
                        fontFamily = montserratFamily,
                        fontSize = 14.sp,
                    )
                }

            }
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Surface(
                    color = GRAY01,
                    shape = RoundedCornerShape(38.dp),
                    modifier = Modifier
                        .width(184.dp)
                        .height(42.dp),
                    shadowElevation = 2.dp,
                    onClick = { onSubmitAnother() }
                ) {
                    Text(
                        text = "SUBMIT ANOTHER",
                        fontFamily = montserratFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(12.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .width(184.dp)
                        .clickable(onClick = { onReturnHome() }),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        8.dp,
                        Alignment.CenterHorizontally
                    ),
                ) {
                    Text(
                        text = "Return to Home",
                        fontFamily = montserratFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = GRAY04
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Right Arrow",
                        tint = GRAY05,
                        modifier = Modifier
                            .height(20.dp)

                    )
                }

            }
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun ReportSubmittedScreenPreview() {
    ReportSubmittedScreen()
}