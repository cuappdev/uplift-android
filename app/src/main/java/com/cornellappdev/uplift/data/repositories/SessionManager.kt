package com.cornellappdev.uplift.data.repositories

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val tokenManager: TokenManager
) {
    // A reactive flow that the UI can collect
    private val _isLoggedIn = MutableStateFlow(tokenManager.getAccessToken() != null)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    // Call this after LoginUser or CreateUser mutations succeed
    fun startSession(userId: Int, name: String, email: String, access: String, refresh: String) {
        tokenManager.saveTokens(access, refresh)
        tokenManager.saveUserSession(userId, name, email)
        _isLoggedIn.value = true
    }

    // Call this for manual logout or when refresh fails
    fun logout() {
        tokenManager.clearTokens()
        _isLoggedIn.value = false
    }

    val userId: Int? get() = tokenManager.getUserId()
}