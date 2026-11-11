package com.cornellappdev.uplift.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.theme.UpliftTheme
import com.cornellappdev.uplift.util.GRAY04
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.PRIMARY_YELLOW
import com.cornellappdev.uplift.util.montserratFamily

@Composable
internal fun ProfileError(onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_profile_inactive),
            contentDescription = null,
            tint = GRAY04,
            modifier = Modifier.size(86.dp)
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.profile_error_title),
            fontFamily = montserratFamily,
            fontSize = 24.sp,
            fontWeight = FontWeight(700),
            color = PRIMARY_BLACK,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.profile_error_message),
            fontFamily = montserratFamily,
            fontSize = 14.sp,
            fontWeight = FontWeight(400),
            color = GRAY04,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(24.dp))

        Button(
            shape = RoundedCornerShape(24.dp),
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = PRIMARY_YELLOW),
            elevation = ButtonDefaults.buttonElevation(0.dp)
        ) {
            Text(
                text = stringResource(R.string.retry),
                fontFamily = montserratFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight(700),
                color = PRIMARY_BLACK
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileErrorPreview() {
    UpliftTheme {
        ProfileError(onRetry = {})
    }
}
