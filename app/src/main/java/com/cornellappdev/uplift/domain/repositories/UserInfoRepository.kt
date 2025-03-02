package com.cornellappdev.uplift.domain.repositories

import com.cornellappdev.uplift.data.models.UserInfo
import com.google.firebase.auth.FirebaseUser

interface UserInfoRepository {
    suspend fun createUser(email: String, name: String, netId: String): Boolean
    suspend fun getUserByNetId(netId: String): UserInfo
    fun hasFirebaseUser(): Boolean
    suspend fun getFirebaseUser() : FirebaseUser?
    suspend fun hasUser(netId: String): Boolean
    suspend fun signInWithGoogle(idToken: String)
    fun signOut()
    suspend fun getUserInfoFromDataStore(): UserInfo
    suspend fun storeSkip(skip: Boolean)
    suspend fun getUserIdFromDataStore(): String
    suspend fun getUserNameFromDataStore(): String
    suspend fun getNetIdFromDataStore(): String
    suspend fun getEmailFromDataStore(): String
    suspend fun getSkipFromDataStore(): Boolean
}