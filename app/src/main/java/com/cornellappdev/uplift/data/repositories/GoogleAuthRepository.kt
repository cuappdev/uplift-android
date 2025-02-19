package com.cornellappdev.uplift.data.repositories

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.cornellappdev.uplift.BuildConfig
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleAuthRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val firebaseAuth: FirebaseAuth,
    @ApplicationContext private val context: Context,
) {

    /**
     * Constructs and returns a GoogleSignInClient.
     */
    val googleSignInClient =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_AUTH_CLIENT_ID)
            .requestEmail()
            .build()

    /**
     * Returns the current [GoogleSignInAccount] if logged in, null otherwise.
     */
    fun accountOrNull(): GoogleSignInAccount? {
        return GoogleSignIn.getLastSignedInAccount(context)
    }

    /**
     * Returns if the user is logged in or not. Identical to `accountOrNull() != null`.
     */
    fun isLoggedIn(): Boolean {
        return accountOrNull() != null
    }

    /**
     * Sign out. Call if the user logs in with a non-cornell email, or whenever a log out should occur.
     *
     * After calling this, the user will no longer auto-navigate to home, and google auth will
     * correctly query for a new email.
     */
    fun signOut() {
        firebaseAuth.signOut()
        GoogleSignIn.getClient(context, googleSignInClient).signOut()
    }

    /**
     * A contract for google login.
     * @param googleSignInClient The google sign in client.
     */
    class AuthResultContract(private val googleSignInClient: GoogleSignInClient) :
        ActivityResultContract<Int, Task<GoogleSignInAccount>?>() {
        override fun parseResult(resultCode: Int, intent: Intent?): Task<GoogleSignInAccount>? {
            return when (resultCode) {
                Activity.RESULT_OK -> GoogleSignIn.getSignedInAccountFromIntent(intent)
                else -> null
            }
        }

        override fun createIntent(context: Context, input: Int): Intent {
            return googleSignInClient.signInIntent.putExtra("input", input)
        }
    }

    /**
     * Returns an activity result launcher for google login.
     *
     * @param onError The error callback.
     * @param onGoogleSignInCompleted The success callback. Takes in the id token and email.
     */
    @Composable
    fun googleLoginLauncher(
        onError: () -> Unit,
        onGoogleSignInCompleted: (id: String, email: String) -> Unit,
    ): ManagedActivityResultLauncher<Intent, ActivityResult> {
        val scope = rememberCoroutineScope()
        return rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
                scope.launch {
                    val authResult = firebaseAuth.signInWithCredential(credential).await()
                    Log.d("GoogleAuthRepository", "Success: ${authResult.user!!.uid}")
                    onGoogleSignInCompleted(
                        account.idToken!!,
                        account.email!!
                    )
                }
            } catch (e: ApiException) {
                e.printStackTrace()
                onError()
            }
        }
    }
}