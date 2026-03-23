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
import javax.inject.Named

@Singleton
class UserInfoRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    @Named("main") private val apolloClient: ApolloClient,
    private val dataStore: DataStore<Preferences>,
    private val sessionManager: SessionManager
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
            val id = userFields.id.toIntOrNull()
            if (id == null) {
                Log.e("UserInfoRepository", "Failed to set goal: non-numeric user ID '$id'")
                return false
            }
            val loginData = loginResponse.data?.loginUser
            if (loginData?.accessToken == null || loginData.refreshToken == null) {
                Log.e("UserInfoRepository", "Login failed after creation: ${loginResponse.errors}")
                return false
            }
            val accessToken = loginData.accessToken
            val refreshToken = loginData.refreshToken
            sessionManager.startSession(
                userId = id,
                name = name,
                email = email,
                access = accessToken,
                refresh = refreshToken
            )
            if (!skip) {
                if (!uploadGoal(id, goal)) {
                    return false
                }
            }
            else {
                Log.d("UserInfoRepository", "Skipping goal upload")
            }
            storeUserFields(
                id = userFields.id,
                username = userFields.name,
                netId = userFields.netId,
                email = userFields.email ?: email,
                goalSkip = skip,
                goal = goal
            )
            Log.d("UserInfoRepositoryImpl", "User created successfully")
            return true
        } catch (e: Exception) {
            Log.e("UserInfoRepositoryImpl", "Error creating user: $e")
            return false
        }
    }

    suspend fun loginUser(netId: String) : Boolean {
        return try {
            val loginResponse = apolloClient.mutation(
                LoginUserMutation(
                    netId = netId
                )
            ).execute()
            val loginData = loginResponse.data?.loginUser
            val userInfo = getUserByNetId(netId)
            if (loginData?.accessToken != null && loginData.refreshToken != null && userInfo != null) {
                val id = userInfo.id.toIntOrNull()
                if (id == null) {
                    Log.e("UserInfoRepository", "Failed to log in: non-numeric user ID '$id'")
                    return false
                }
                sessionManager.startSession(
                    userId = id,
                    name = userInfo.name,
                    email = userInfo.email,
                    access = loginData.accessToken,
                    refresh = loginData.refreshToken
                )
                true
            } else {
                Log.e("UserInfoRepository", "Login failed: Missing tokens or user info;  ${loginResponse.errors}")
                false
            }
        } catch (e: Exception) {
            Log.e("UserInfoRepository", "Error logging in: $e")
            false
        }
    }

    suspend fun uploadGoal(id:Int?, goal: Int): Boolean {
        if (id == null) {
            Log.e("UserInfoRepository", "Failed to set goal: non-numeric user ID '$id'")
            return false
        }
        val goalResponse = apolloClient.mutation(
            SetWorkoutGoalsMutation(
                userId = id,
                workoutGoal = goal
            )
        )
            .execute()
        if (goalResponse.hasErrors()) {
            Log.e("UserInfoRepository", "Failed to set goal: ${goalResponse.errors}")
            return false
        }
        Log.d("UserInfoRepository", "Goal set successfully: $goal")
        return true
    }


    suspend fun storeUserFields(id: String, username: String, netId: String, email: String, goalSkip: Boolean, goal: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.ID] = id
            preferences[PreferencesKeys.NETID] = netId
            preferences[PreferencesKeys.USERNAME] = username
            preferences[PreferencesKeys.EMAIL] = email
            preferences[PreferencesKeys.GOAL_SETTING_SKIPPED] = goalSkip
            if (!goalSkip) {
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
                encodedImage = user.encodedImage,
                activeStreak = user.activeStreak,
                maxStreak = user.maxStreak,
                workoutGoal = user.workoutGoal,
                streakStart = user.streakStart?.toString(),
                totalGymDays = user.totalGymDays
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

    suspend fun storeSkip(skip: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.GOAL_SETTING_SKIPPED] = skip
        }
    }


    suspend fun getSkipFromDataStore(): Boolean {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.GOAL_SETTING_SKIPPED]
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
