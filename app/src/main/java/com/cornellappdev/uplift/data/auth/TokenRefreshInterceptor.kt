package com.cornellappdev.uplift.data.auth

import com.apollographql.apollo.api.ApolloRequest
import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.interceptor.ApolloInterceptor
import com.apollographql.apollo.interceptor.ApolloInterceptorChain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * The backend reports an expired access token as a GraphQL error with an HTTP 200, so OkHttp's
 * [TokenAuthenticator] never fires for it. This refreshes the token and retries the operation once.
 */
@Singleton
class TokenRefreshInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
    private val tokenRefresher: TokenRefresher
) : ApolloInterceptor {

    override fun <D : Operation.Data> intercept(
        request: ApolloRequest<D>,
        chain: ApolloInterceptorChain
    ): Flow<ApolloResponse<D>> = flow {
        val sentToken = tokenManager.getAccessToken()
        var retried = false

        chain.proceed(request).collect { response ->
            if (!retried && sentToken != null && response.hasExpiredTokenError()) {
                retried = true
                if (tokenRefresher.refresh(sentToken) != null) {
                    // AuthInterceptor attaches the refreshed token from TokenManager on retry.
                    emitAll(chain.proceed(request))
                    return@collect
                }
            }
            emit(response)
        }
    }

    private fun ApolloResponse<*>.hasExpiredTokenError(): Boolean =
        errors.orEmpty().any { error ->
            EXPIRED_TOKEN_MESSAGES.any { error.message.contains(it, ignoreCase = true) }
        }

    private companion object {
        val EXPIRED_TOKEN_MESSAGES = listOf("Signature has expired", "Token has expired")
    }
}
