package com.cornellappdev.uplift.data.auth

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton
import com.cornellappdev.uplift.di.AppModule.ApplicationScope

@Singleton
class SessionManager @Inject constructor(
    private val tokenManager: TokenManager,
    @ApplicationScope private val upliftScope: CoroutineScope
) {
    // A reactive flow that the UI can collect
    val isLoggedIn: StateFlow<Boolean> = tokenManager.tokenFlow
        .map { token -> token != null }
        .stateIn(
            scope = upliftScope,
            started = SharingStarted.Eagerly,
            initialValue = tokenManager.getAccessToken() != null
        )

    // Call this after LoginUser or CreateUser mutations succeed
    fun startSession(userId: Int, name: String, email: String, access: String, refresh: String) {
        tokenManager.saveTokens(access, refresh)
        tokenManager.saveUserSession(userId, name, email)
    }

    // Call this for manual logout or when refresh fails
    fun logout() {
        tokenManager.clearTokensAndUserInfo()
    }

    val userId: Int? get() = tokenManager.getUserId()
}