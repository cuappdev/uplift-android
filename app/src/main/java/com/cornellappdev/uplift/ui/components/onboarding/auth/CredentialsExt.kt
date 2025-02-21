package com.cornellappdev.uplift.ui.components.onboarding.auth

import android.content.Context
import android.util.Log
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.NoCredentialException
import com.cornellappdev.uplift.util.PRIMARY_YELLOW
import com.cornellappdev.uplift.util.montserratFamily
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.cornellappdev.uplift.BuildConfig
import kotlinx.coroutines.launch

@Composable
fun LogInButton(onRequestResult: (Credential) -> Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    Button(
        onClick = {
            coroutineScope.launch {
                launchCredentialManagerButtonUI(
                    context,
                    onRequestResult
                )
            }
        },
        elevation = ButtonDefaults.elevation(5.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = PRIMARY_YELLOW)
    ) {
        Text(
            "Log in",
            fontSize = 16.sp,
            fontFamily = montserratFamily,
            fontWeight = FontWeight.Bold
        )
    }
}

private suspend fun launchCredentialManagerButtonUI(
    context: Context,
    onRequestResult: (Credential) -> Unit
) {
    try {
        val signInWithGoogleOption = GetSignInWithGoogleOption
            .Builder(serverClientId = BuildConfig.GOOGLE_AUTH_CLIENT_ID)
            .build()
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(signInWithGoogleOption)
            .build()
        val result = CredentialManager.create(context).getCredential(
            request = request,
            context = context
        )
        onRequestResult(result.credential)
    } catch (e: NoCredentialException) {
        // TODO: Handle no credential found with toast or snackbar
        Log.e("CredentialManager", "No accounts error", e)
    } catch (e: Exception) {
        Log.e("CredentialManager", e.message.orEmpty(), e)
    }

}