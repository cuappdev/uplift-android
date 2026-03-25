package com.cornellappdev.uplift.data.auth

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.cornellappdev.uplift.RefreshAccessTokenMutation
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    private val sessionManager: SessionManager,
    @Named("refresh") private val apolloClient: ApolloClient
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 2) {
            return null
        }
        
        val refreshToken = tokenManager.getRefreshToken() ?: return null

        synchronized(this) {
            // Check if the token was already refreshed by another thread
            // while this request was waiting for the lock.
            val currentToken = tokenManager.getAccessToken()
            val requestToken = response.request.header("Authorization")?.substringAfter("Bearer ")

            if (currentToken != null && currentToken != requestToken ) {
                return response.request.newBuilder()
                    .header("Authorization", "Bearer $currentToken")
                    .build()
            }

            // 3. Since OkHttp's Authenticator is synchronous but Apollo is suspend-based,
            // we use runBlocking to wait for the refresh mutation result.
            return runBlocking {
                try {
                    val mutationResponse = withTimeout(10000L) {
                        apolloClient.mutation(RefreshAccessTokenMutation())
                            // We manually add the Refresh Token to this specific call
                            // because the "refresh" ApolloClient has no interceptor.
                            .addHttpHeader("Authorization", "Bearer $refreshToken")
                            .execute()
                    }

                    val newAccessToken = mutationResponse.data?.refreshAccessToken?.newAccessToken

                    if (newAccessToken != null && newAccessToken != requestToken) {
                        tokenManager.saveTokens(newAccessToken, refreshToken)

                        // Retry the original request with the new Access Token
                        response.request.newBuilder()
                            .header("Authorization", "Bearer $newAccessToken")
                            .build()
                    } else {
                        // Refresh failed (e.g., refresh token expired on backend)
                        sessionManager.logout()
                        null
                    }
                } catch (e: Exception) {
                    // Network error or server down during refresh
                    Log.e("TokenAuthenticator", "Refresh timed out or failed", e)
                    sessionManager.logout()
                    null
                }
            }
        }
    }

    private fun responseCount(response: Response?): Int {
        var result = 1
        var current = response
        // Traverse the chain of prior responses
            while (current?.priorResponse != null) {
                result++
                current = current.priorResponse
            }
            return result
        }
}