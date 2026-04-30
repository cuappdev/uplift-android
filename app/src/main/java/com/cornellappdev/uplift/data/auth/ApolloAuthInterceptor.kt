package com.cornellappdev.uplift.data.auth

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloRequest
import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.ExecutionContext
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.interceptor.ApolloInterceptor
import com.apollographql.apollo.interceptor.ApolloInterceptorChain
import com.cornellappdev.uplift.RefreshAccessTokenMutation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

/**
 * Execution context to track retries.
 */
internal class RetryContext(val retryCount: Int) : ExecutionContext.Element {
    override val key: ExecutionContext.Key<*> = Key

    companion object Key : ExecutionContext.Key<RetryContext>
}

/**
 * An Apollo Interceptor that handles token expiration errors that return as 200 OK with
 * GraphQL errors (specifically "Signature has expired").
 */
@Singleton
class ApolloAuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
    private val sessionManager: SessionManager,
    @Named("refresh") private val refreshClient: ApolloClient
) : ApolloInterceptor {
    private val mutex = Mutex()

    override fun <D : Operation.Data> intercept(
        request: ApolloRequest<D>,
        chain: ApolloInterceptorChain
    ): Flow<ApolloResponse<D>> = flow {
        val response = chain.proceed(request).first()

        val retryCount = request.executionContext[RetryContext]?.retryCount ?: 0

        // TODO: replace string check with explicit error codes if backend implements
        if (response.errors?.any { it.message.contains("Signature has expired") } == true && retryCount < 1) {
            val refreshToken = tokenManager.getRefreshToken()
            if (refreshToken != null) {
                val newAccessToken = mutex.withLock {
                    // Check if another request already refreshed the token while we were waiting for the lock
                    val currentAccessToken = tokenManager.getAccessToken()
                    val requestToken = request.httpHeaders?.find { it.name == "Authorization" }?.value?.substringAfter("Bearer ")

                    if (currentAccessToken != null && currentAccessToken != requestToken) {
                        currentAccessToken
                    } else {
                        try {
                            val mutationResponse = refreshClient.mutation(RefreshAccessTokenMutation())
                                .addHttpHeader("Authorization", "Bearer $refreshToken")
                                .execute()

                            val refreshedToken = mutationResponse.data?.refreshAccessToken?.newAccessToken
                            if (refreshedToken != null) {
                                tokenManager.saveTokens(refreshedToken, refreshToken)
                                refreshedToken
                            } else {
                                Log.e("ApolloAuthInterceptor", "Refresh token mutation returned null access token")
                                sessionManager.logout()
                                null
                            }
                        } catch (e: Exception) {
                            Log.e("ApolloAuthInterceptor", "Token refresh failed with exception", e)
                            sessionManager.logout()
                            null
                        }
                    }
                }

                if (newAccessToken != null) {
                    // Retry the request with the new token
                    val newRequest = request.newBuilder()
                        .addExecutionContext(RetryContext(retryCount + 1))
                        .build()
                    emitAll(chain.proceed(newRequest))
                    return@flow
                }
            } else {
                Log.d("ApolloAuthInterceptor", "No refresh token available, logging out")
                sessionManager.logout()
            }
        }

        emit(response)
    }
}
