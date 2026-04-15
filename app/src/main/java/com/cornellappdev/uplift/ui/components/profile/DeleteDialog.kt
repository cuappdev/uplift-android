package com.cornellappdev.uplift.ui.components.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun DeleteAccountConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 24.dp)
            ) {
                // Top-right Close Button
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .clickable { onDismiss() }
                        .size(24.dp),
                    tint = Color.Black.copy(alpha = 0.6f)
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Top Center Trash Icon
                    Icon(
                        painterResource(id = R.drawable.ic_trash),
                        contentDescription = "Delete account",
                        modifier = Modifier.size(40.dp),
                        tint = Color.Black
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Are you sure you want to delete your Uplift account?",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight(600),
                            color = Color.Black,
                            lineHeight = 22.sp
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "All workout data will be lost.",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight(400),
                            color = Color.Black
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Delete Button (Black)
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    ) {
                        Text(
                            text = "Delete",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight(700)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Back Button
                    Text(
                        text = "Back",
                        modifier = Modifier
                            .clickable { onDismiss() }
                            .padding(vertical = 4.dp),
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight(700),
                            color = Color.Black
                        )
                    )
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