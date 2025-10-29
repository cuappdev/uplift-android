package com.cornellappdev.uplift.ui.components.general

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.viewmodels.profile.CheckInMode


@Composable
fun CheckInPopUp(
    gymName: String,
    currentTimeText: String,
    onDismiss: () -> Unit,
    onCheckIn: () -> Unit,
    onClosePopUp: () -> Unit,
    mode: CheckInMode
) {
    Row(
        modifier = Modifier
            .shadow(
                elevation = 40.dp,
                spotColor = Color(0xFFE5ECED),
                ambientColor = Color(0xFFE5ECED)
            )
            .fillMaxWidth()
            .height(62.dp)
            .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(size = 12.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AnimatedContent(
            targetState = mode,
            transitionSpec = {
                (fadeIn(tween(300)) togetherWith fadeOut(tween(300)))
                    .using(SizeTransform(false))
            },
            label = "check-in"
        ) { target ->
            if (target == CheckInMode.Prompt) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                    ) {
                        Text(
                            text = "We see you're near a gym...",
                            style = TextStyle(
                                fontSize = 14.sp,
                                lineHeight = 16.sp,
                                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                                fontWeight = FontWeight(600),
                                color = Color(0xFF222222),

                                )
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "$gymName at $currentTimeText",
                            style = TextStyle(
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                                fontWeight = FontWeight(400),
                                color = Color(0xFF707070),

                                )

                        )
                    }
                    Row(
                        modifier = Modifier
                            .height(34.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            modifier = Modifier
                                .width(93.dp)
                                .height(34.dp),
                            shape = RoundedCornerShape(size = 11.05263.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFFFCF5A4),
                                contentColor = Color.Black
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
                            onClick = onCheckIn
                        ) {
                            Text(
                                text = "Check In?",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    lineHeight = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                                    fontWeight = FontWeight(500),
                                    color = Color(0xFF000000),
                                )
                            )
                        }

                        Image(
                            painter = painterResource(id = R.drawable.ic_close_popup),
                            contentDescription = "close pop up",
                            contentScale = ContentScale.None,
                            modifier = Modifier.clickable { onDismiss() }
                        )
                    }


                }

            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "You’re all set. Enjoy your workout!",
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 16.sp,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                            fontWeight = FontWeight(600),
                            color = Color(0xFF222222),
                        )
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_close_popup),
                        contentDescription = "close pop up",
                        contentScale = ContentScale.None,
                        modifier = Modifier.clickable { onClosePopUp() }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CheckInPopUpPreview(
){
    CheckInPopUp("Helen Newman",
   "1:00 PM", {}, {}, {}, CheckInMode.Complete)
}