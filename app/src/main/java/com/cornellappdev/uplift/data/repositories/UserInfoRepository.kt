package com.cornellappdev.uplift.data.repositories;

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Base64
import android.util.Log
import com.apollographql.apollo.api.Optional
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import kotlin.math.roundToInt
import javax.inject.Inject
import javax.inject.Singleton
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.Preferences
import com.apollographql.apollo.ApolloClient
import com.cornellappdev.uplift.CreateUserMutation
import com.cornellappdev.uplift.DeleteUserMutation
import com.cornellappdev.uplift.GetUserByNetIdQuery
import com.cornellappdev.uplift.LoginUserMutation
import com.cornellappdev.uplift.SetWorkoutGoalsMutation
import com.cornellappdev.uplift.data.auth.SessionManager
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
    private val sessionManager: SessionManager,
    @ApplicationContext private val context: Context
){

    suspend fun createUser(
        email: String,
        name: String,
        netId: String,
        skip: Boolean,
        goal: Int,
        imageUri: Uri? = null
    ): Boolean {
        try{
            // Encode before creating the account so a photo-read failure cannot silently lose it
            val encodedImage = imageUri?.let { encodeProfileImage(it) }
            val response = apolloClient.mutation(
                CreateUserMutation(
                    email = email,
                    name = name,
                    netId = netId,
                    encodedImage = Optional.presentIfNotNull(encodedImage),
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
                Log.e("UserInfoRepository", "Failed to set goal: non-numeric user ID '${userFields.id}'")
                return false
            }
            val loginData = loginResponse.data?.loginUser
            if (loginData?.accessToken == null || loginData.refreshToken == null) {
                Log.e("UserInfoRepository", "Login failed after creation: ${loginResponse.errors}")
                return false
            }
            val accessToken = loginData.accessToken
            val refreshToken = loginData.refreshToken
            if (!skip) {
                if (!uploadGoal(id, goal, accessToken)) {
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
            sessionManager.startSession(
                userId = id,
                name = name,
                email = email,
                netId = netId,
                access = accessToken,
                refresh = refreshToken
            )
            Log.d("UserInfoRepositoryImpl", "User created successfully")
            return true
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("UserInfoRepositoryImpl", "Error creating user: $e")
            return false
        }
    }

    private suspend fun encodeProfileImage(uri: Uri): String = withContext(Dispatchers.IO) {
        val source = ImageDecoder.createSource(context.contentResolver, uri)
        // preserves aspect ratio, caps the longest side at 300px, and use 20% JPEG
        val bitmap = ImageDecoder.decodeBitmap(source) { decoder, info, _ ->
            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
            val longestSide = maxOf(info.size.width, info.size.height)
            if (longestSide > 300) {
                val scale = 300.0 / longestSide
                decoder.setTargetSize(
                    (info.size.width * scale).roundToInt().coerceAtLeast(1),
                    (info.size.height * scale).roundToInt().coerceAtLeast(1)
                )
            }
        }
        try {
            ByteArrayOutputStream().use { output ->
                check(bitmap.compress(Bitmap.CompressFormat.JPEG, 20, output)) {
                    "Failed to compress profile image"
                }
                Base64.encodeToString(output.toByteArray(), Base64.NO_WRAP)
            }
        } finally {
            bitmap.recycle()
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
                    Log.e("UserInfoRepository", "Failed to log in: non-numeric user ID resulting in null '${userInfo.id}'")
                    return false
                }
                storeUserFields(
                    id = userInfo.id,
                    username = userInfo.name,
                    netId = netId,
                    email = userInfo.email,
                    goalSkip = false, // Defaulting to false on login if not known
                    goal = userInfo.workoutGoal ?: 0
                )
                sessionManager.startSession(
                    userId = id,
                    name = userInfo.name,
                    email = userInfo.email,
                    netId = netId,
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

    suspend fun uploadGoal(id:Int?, goal: Int, manualToken: String? = null): Boolean {
        if (id == null) {
            Log.e("UserInfoRepository", "Failed to set goal: non-numeric user ID '$id'")
            return false
        }
        val call = apolloClient.mutation(
            SetWorkoutGoalsMutation(
                userId = id,
                workoutGoal = goal
            )
        )
        if (manualToken != null) {
            call.addHttpHeader("Authorization", "Bearer $manualToken")
        }
        val goalResponse = call.execute()
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

    suspend fun signOut() {
        try {
            firebaseAuth.signOut()
        } catch (e: Exception) {
            Log.e("UserInfoRepository", "Firebase signout failed: $e")
        } finally {
            sessionManager.logout()
            dataStore.edit { it.clear() }
        }
    }

    suspend fun deleteAccount(): Boolean {
        return try {
            val userId = sessionManager.userId
            if (userId == null) {
                Log.e("UserInfoRepository", "Delete account failed: No user ID found in session")
                return false
            }

            val response = apolloClient.mutation(DeleteUserMutation(userId = userId)).execute()
            if (response.hasErrors()) {
                Log.e("UserInfoRepository", "Server error during delete: ${response.errors}")
                return false
            }

            // Want to log out no matter if the firebase deletes or not
            try {
                firebaseAuth.currentUser?.delete()?.await()
                Log.d("UserInfoRepository", "Firebase account deleted successfully")
            } catch (e: Exception) {
                Log.e("UserInfoRepository", "Firebase delete failed (User may need to re-login): $e")
            }

            sessionManager.logout()
            dataStore.edit { it.clear() }

            Log.d("UserInfoRepository", "Successfully signed out")
            true
        } catch (e: Exception) {
            Log.e("UserInfoRepository", "Error deleting account: $e")
            false
        }
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
        }.firstOrNull() ?: sessionManager.netId
    }

    suspend fun getEmailFromDataStore(): String? {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.EMAIL]
        }.firstOrNull()
    }

}
