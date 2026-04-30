package com.cornellappdev.uplift.data.auth

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
                try {
                    val mutationResponse = refreshClient.mutation(RefreshAccessTokenMutation())
                        .addHttpHeader("Authorization", "Bearer $refreshToken")
                        .execute()

                    val newAccessToken = mutationResponse.data?.refreshAccessToken?.newAccessToken
                    if (newAccessToken != null) {
                        tokenManager.saveTokens(newAccessToken, refreshToken)
                        // Retry the request with the new token
                        val newRequest = request.newBuilder()
                            .addExecutionContext(RetryContext(retryCount + 1))
                            .build()
                        emitAll(chain.proceed(newRequest))
                        return@flow
                    } else {
                        sessionManager.logout()
                    }
                } catch (e: Exception) {
                    sessionManager.logout()
                }
            } else {
                sessionManager.logout()
            }
        }

        emit(response)
    }
}
