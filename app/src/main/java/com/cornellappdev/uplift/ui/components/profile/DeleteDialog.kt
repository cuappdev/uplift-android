package com.cornellappdev.uplift.ui.components.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.components.general.UpliftButton
import com.cornellappdev.uplift.ui.components.goalsetting.CloseButton
import com.cornellappdev.uplift.ui.theme.AppColors
import com.cornellappdev.uplift.util.montserratFamily

@Composable
fun DeleteAccountConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = AppColors.White,
            modifier = Modifier.width(250.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                CloseButton(
                    tint = AppColors.Black.copy(alpha = 0.4f),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 12.dp, end = 12.dp)
                        .size(24.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onDismiss()
                        },
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_trash_2),
                        contentDescription = "Delete account",
                        modifier = Modifier.size(36.dp),
                        tint = AppColors.Black
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Are you sure you want to delete your Uplift account?",
                            style = TextStyle(
                                color = AppColors.Black,
                                textAlign = TextAlign.Center,
                                fontFamily = montserratFamily,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W400,
                                lineHeight = 18.sp
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        Text(
                            text = "All workout data will be lost.",
                            style = TextStyle(
                                color = AppColors.Black,
                                textAlign = TextAlign.Center,
                                fontFamily = montserratFamily,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W500,
                                lineHeight = 18.sp
                            )
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        UpliftButton(
                            onClick = onConfirm,
                            text = "Delete",
                            modifier = Modifier
                                .fillMaxWidth(),
                            height = 41.dp,
                            containerColor = AppColors.Black,
                            contentColor = AppColors.White,
                            fontSize = 16f,
                            elevation = 0.dp
                        )

                        UpliftButton(
                            onClick = onDismiss,
                            text = "Back",
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            containerColor = Color.Transparent,
                            contentColor = AppColors.Black,
                            fontSize = 14f,
                            elevation = 0.dp
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DeleteAccountConfirmationDialogPreview() {
    DeleteAccountConfirmationDialog(
        onDismiss = {},
        onConfirm = {}
    )
}
