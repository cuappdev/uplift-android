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
import com.cornellappdev.uplift.LoginUserMutation
import com.cornellappdev.uplift.SetWorkoutGoalsMutation
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
    private val tokenManager: TokenManager
){

    suspend fun createUser(email: String, name: String, netId: String, skip: Boolean, goal: Int): Boolean {
        try{
            val response = apolloClient.mutation(
                CreateUserMutation(
                    email = email,
                    name = name,
                    netId = netId,
                )
            ).execute()
            val userFields = response.data?.createUser?.userFields
            if (response.hasErrors() || userFields == null) {
                Log.e("UserInfoRepository", "Server error: ${response.errors}")
                return false
            }
            val loginResponse = apolloClient.mutation(
                LoginUserMutation(
                    netId = netId
                )
            ).execute()
            val id = userFields.id
            val loginData = loginResponse.data?.loginUser
            if (loginData?.accessToken == null || loginData.refreshToken == null) {
                Log.e("UserInfoRepository", "Login failed after creation: ${loginResponse.errors}")
                return false
            }
            val accessToken = loginData.accessToken
            val refreshToken = loginData.refreshToken
            tokenManager.saveTokens(accessToken, refreshToken)
            if (!skip) {
                val numericId = id.toIntOrNull()
                uploadGoal(numericId, goal)
            }
            storeUserFields(id, name, netId, email, skip, accessToken, refreshToken, goal)
            Log.d("UserInfoRepositoryImpl", "User created successfully")
            return true
        } catch (e: Exception) {
            Log.e("UserInfoRepositoryImpl", "Error creating user: $e")
            return false
        }
    }

    suspend fun uploadGoal(id:Int?, goal: Int): Boolean {
        if (id == null) {
            Log.e("UserInfoRepository", "Failed to set goal: non-numeric user ID '$id'")
            return false
        }
        val goalResponse = apolloClient.mutation(
            SetWorkoutGoalsMutation(
                id = id,
                workoutGoal = goal
            )
        )
            // Change so that it uses auth interceptor
            .addHttpHeader("Authorization", "Bearer ${tokenManager.getAccessToken()}")
            .execute()
        if (goalResponse.hasErrors()) {
            Log.e("UserInfoRepository", "Failed to set goal: ${goalResponse.errors}")
            return false
        }
        return true
    }


    suspend fun storeUserFields(id: String, username: String, netId: String, email: String, skip: Boolean, accessToken: String, refreshToken: String, goal: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.ID] = id
            preferences[PreferencesKeys.NETID] = netId
            preferences[PreferencesKeys.USERNAME] = username
            preferences[PreferencesKeys.EMAIL] = email
            preferences[PreferencesKeys.SKIP] = skip
            preferences[PreferencesKeys.ACCESS_TOKEN] = accessToken
            preferences[PreferencesKeys.REFRESH_TOKEN] = refreshToken
            if (!skip) {
                preferences[PreferencesKeys.GOAL] = goal
            }
        }
    }

    suspend fun getUserByNetId(netId: String): UserInfo? {
        try {
            val response = apolloClient.query(
                GetUserByNetIdQuery(
                    netId = netId
                )
            ).execute()
            val user = response.data?.getUserByNetId?.firstOrNull()?.userFields ?: return null
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

    fun getFirebaseUser(): FirebaseUser? {
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

    private suspend fun storeGoal(goal: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.GOAL] = goal
        }
    }

    suspend fun storeSkip(skip: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SKIP] = skip
        }
    }

    private suspend fun storeAccessToken(accessToken: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.ACCESS_TOKEN] = accessToken
        }
    }

    private suspend fun storeRefreshToken(refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.REFRESH_TOKEN] = refreshToken
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
