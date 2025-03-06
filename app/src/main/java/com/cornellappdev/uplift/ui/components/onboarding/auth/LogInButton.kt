package com.cornellappdev.uplift.ui.components.onboarding.auth

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.cornellappdev.uplift.BuildConfig
import com.cornellappdev.uplift.ui.components.general.UpliftButton
import kotlinx.coroutines.launch

@Composable
fun LogInButton(onRequestResult: (Credential) -> Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    UpliftButton(
        onClick = {
            coroutineScope.launch {
                launchCredentialManagerButtonUI(
                    context,
                    onRequestResult
                )
            }
        },
        text = "Log in",
        width = 144.dp,
        height = 44.dp,
        fontSize = 16f,
        elevation = 2.dp
    )
}

// TODO: Try to move parts that don't require Context to viewmodel
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