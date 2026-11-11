package com.cornellappdev.uplift.data.auth

import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val tokenRefresher: TokenRefresher
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 2) {
            return null
        }

        val requestToken = response.request.header("Authorization")?.substringAfter("Bearer ")

        // Since OkHttp's Authenticator is synchronous but the refresh is suspend-based,
        // use runBlocking to wait for the refresh result.
        val newAccessToken = runBlocking { tokenRefresher.refresh(requestToken) } ?: return null

        // Retry the original request with the new Access Token
        return response.request.newBuilder()
            .header("Authorization", "Bearer $newAccessToken")
            .build()
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
