package com.cornellappdev.uplift.data.repositories;

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.Preferences
import com.apollographql.apollo.ApolloClient
import com.cornellappdev.uplift.CreateUserMutation
import com.cornellappdev.uplift.GetUserByNetIdQuery
import kotlinx.coroutines.flow.map;
import kotlinx.coroutines.flow.firstOrNull
import com.cornellappdev.uplift.data.models.UserInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

@Singleton
class UserInfoRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val apolloClient: ApolloClient,
    private val dataStore: DataStore<Preferences>,
){

    suspend fun createUser(email: String, name: String, netId: String): Boolean {
        try{
            val response = apolloClient.mutation(
                CreateUserMutation(
                    email = email,
                    name = name,
                    netId = netId,
                )
            ).execute()
            storeId(response.data?.createUser?.userFields?.let { storeId(it.id) }.toString())
            storeNetId(netId)
            storeUsername(name)
            storeEmail(email)
            storeSkip(false)
            Log.d("UserInfoRepositoryImpl", "User created successfully" + response.data)
            return true
        } catch (e: Exception) {
            Log.e("UserInfoRepositoryImpl", "Error creating user: $e")
            return false
        }
    }

    suspend fun getUserByNetId(netId: String): UserInfo? {
        try {
            val response = apolloClient.query(
                GetUserByNetIdQuery(
                    netId = netId
                )
            ).executeV3()
            val user = response.data?.getUserByNetId?.get(0)?.userFields ?: return null
            return UserInfo(
                id = user.id,
                name = user.name,
                email = user.email ?: "",
                netId = user.netId,
            )
        } catch (e: Exception) {
            Log.e("UserInfoRepositoryImpl", "Error getting user by netId: $e")
            return null
        }
    }

    fun hasFirebaseUser(): Boolean {
        return firebaseAuth.currentUser != null
    }

    suspend fun getFirebaseUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    suspend fun hasUser(netId: String): Boolean {
        return hasFirebaseUser() && getUserByNetId(netId) != null
    }

    suspend fun signInWithGoogle(idToken: String) {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(firebaseCredential).await()
    }

    fun signOut() {
        firebaseAuth.signOut()
    }

    private suspend fun storeId(id: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.ID] = id
        }
    }

    private suspend fun storeUsername(username: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USERNAME] = username
        }
    }

    private suspend fun storeNetId(netId: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.NETID] = netId
        }
    }

    private suspend fun storeEmail(email: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.EMAIL] = email
        }
    }

    suspend fun storeSkip(skip: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SKIP] = skip
        }
    }


    suspend fun getSkipFromDataStore(): Boolean {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.SKIP]
        }.firstOrNull() ?: false
    }

    suspend fun getUserIdFromDataStore(): String? {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.ID]
        }.firstOrNull()
    }

    suspend fun getUserNameFromDataStore(): String? {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.USERNAME]
        }.firstOrNull()
    }

    suspend fun getNetIdFromDataStore(): String? {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.NETID]
        }.firstOrNull()
    }

    suspend fun getEmailFromDataStore(): String? {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.EMAIL]
        }.firstOrNull()
    }

}
