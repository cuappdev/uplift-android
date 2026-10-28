package com.cornellappdev.uplift.data.auth

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.cornellappdev.uplift.RefreshAccessTokenMutation
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeout
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds

/**
 * Shared access token refresh logic, used by both [TokenAuthenticator] (HTTP 401s) and
 * [TokenRefreshInterceptor] (expired-token GraphQL errors returned with HTTP 200).
 */
@Singleton
class TokenRefresher @Inject constructor(
    private val tokenManager: TokenManager,
    private val sessionManager: SessionManager,
    @Named("refresh") private val apolloClient: ApolloClient
) {
    private val mutex = Mutex()

    /**
     * Returns an access token to retry with, or null if the session could not be refreshed.
     * [staleToken] is the access token the failed request was sent with.
     */
    suspend fun refresh(staleToken: String?): String? = mutex.withLock {
        val refreshToken = tokenManager.getRefreshToken() ?: return@withLock null

        // Check if the token was already refreshed by another caller
        // while this one was waiting for the lock.
        val currentToken = tokenManager.getAccessToken()
        if (currentToken != null && currentToken != staleToken) {
            return@withLock currentToken
        }

        val newAccessToken = try {
            val mutationResponse = withTimeout(10.seconds) {
                apolloClient.mutation(RefreshAccessTokenMutation())
                    // We manually add the Refresh Token to this specific call
                    // because the "refresh" ApolloClient has no interceptor.
                    .addHttpHeader("Authorization", "Bearer $refreshToken")
                    .execute()
            }
            if (mutationResponse.hasErrors() || mutationResponse.exception != null) {
                Log.e(
                    "TokenRefresher",
                    "Refresh failed: ${mutationResponse.errors}",
                    mutationResponse.exception
                )
            }
            mutationResponse.data?.refreshAccessToken?.newAccessToken
        } catch (e: TimeoutCancellationException) {
            Log.e("TokenRefresher", "Refresh timed out", e)
            null
        }

        if (newAccessToken != null && newAccessToken != staleToken) {
            tokenManager.saveTokens(newAccessToken, refreshToken)
            newAccessToken
        } else {
            // Refresh failed
            sessionManager.logout()
            null
        }
    }
}
