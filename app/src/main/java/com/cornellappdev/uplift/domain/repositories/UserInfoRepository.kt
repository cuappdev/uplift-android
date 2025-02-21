package com.cornellappdev.uplift.domain.repositories

import com.cornellappdev.uplift.data.models.UserInfo

interface UserInfoRepository {
    suspend fun createUser(email: String, name: String, netId: String): Boolean
    suspend fun getUserInfo(): UserInfo
    suspend fun hasUser(): Boolean
    suspend fun signInWithGoogle(idToken: String)
    suspend fun signOut()
    suspend fun getUserId(): String?
    suspend fun getUsername(): String?
    suspend fun getNetId(): String?
    suspend fun getEmail(): String?
}