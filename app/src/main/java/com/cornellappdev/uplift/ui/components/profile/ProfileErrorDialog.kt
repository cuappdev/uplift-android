package com.cornellappdev.uplift.ui.components.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.components.general.UpliftButton
import com.cornellappdev.uplift.ui.theme.AppColors
import com.cornellappdev.uplift.ui.theme.AppTextStyles
import com.cornellappdev.uplift.ui.theme.UpliftTheme

/** Displays a profile error with an optional retry action owned by the caller. */
@Composable
fun ProfileErrorDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    onRetry: (() -> Unit)? = null,
) {
    Dialog(onDismissRequest = onDismiss) {
        ProfileErrorDialogContent(title, message, onDismiss, onRetry)
    }
}

@Composable
private fun ProfileErrorDialogContent(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    onRetry: (() -> Unit)? = null,
) {
    Surface(
        modifier = Modifier.width(280.dp),
        shape = RoundedCornerShape(32.dp),
        color = AppColors.White
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()).padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.img_profile_error),
                contentDescription = null,
                modifier = Modifier.size(87.dp, 117.dp).rotate(8.49f)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = title,
                    style = AppTextStyles.HeadingBold,
                    color = AppColors.Black,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = message,
                    style = AppTextStyles.LabelNormal,
                    color = AppColors.Black,
                    textAlign = TextAlign.Center
                )
            }
            if (onRetry != null) {
                UpliftButton(
                    text = stringResource(R.string.retry),
                    onClick = onRetry,
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = AppColors.Black,
                    contentColor = AppColors.White,
                    elevation = 0.dp
                )
            }
            UpliftButton(
                text = stringResource(R.string.profile_error_dismiss),
                onClick = onDismiss,
                containerColor = Color.Transparent,
                contentColor = AppColors.Black,
                elevation = 0.dp
            )
        }
    }
}

@Preview(name = "Profile creation retry", showBackground = true)
@Composable
private fun ProfileErrorDialogWithRetryPreview() {
    UpliftTheme {
        Box(Modifier.background(AppColors.Gray01).padding(16.dp)) {
            ProfileErrorDialogContent(
                title = stringResource(R.string.profile_create_error_title),
                message = stringResource(R.string.profile_create_error_message),
                onDismiss = {},
                onRetry = {}
            )
        }
    }
}

@Preview(name = "Account deletion retry", showBackground = true)
@Composable
private fun ProfileErrorDialogPreview() {
    UpliftTheme {
        Box(Modifier.background(AppColors.Gray01).padding(16.dp)) {
            ProfileErrorDialogContent(
                title = stringResource(R.string.profile_delete_error_title),
                message = stringResource(R.string.profile_delete_error_message),
                onDismiss = {},
                onRetry = {}
            )
        }
    }
}
