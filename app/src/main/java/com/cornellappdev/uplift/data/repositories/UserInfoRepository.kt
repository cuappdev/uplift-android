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
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
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
import com.cornellappdev.uplift.UpdateProfilePhotoMutation
import com.cornellappdev.uplift.data.mappers.toResult
import com.cornellappdev.uplift.data.auth.SessionManager
import com.cornellappdev.uplift.fragment.UserFields
import kotlinx.coroutines.flow.map;
import kotlinx.coroutines.flow.firstOrNull
import com.cornellappdev.uplift.data.models.UserInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Named
import androidx.core.net.toUri

@Singleton
class UserInfoRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    @Named("main") private val apolloClient: ApolloClient,
    private val dataStore: DataStore<Preferences>,
    private val sessionManager: SessionManager,
    @ApplicationContext private val context: Context
){
    private companion object {
        const val TAG = "UserInfoRepository"
    }

    // Guards createdUserFields and createdImageUri across createUser and signOut
    private val onboardingMutex = Mutex()
    // Backend account created during onboarding whose remaining setup has not yet succeeded
    private var createdUserFields: UserFields? = null
    // Photo the backend account currently has, so a photo changed before resuming can be applied
    private var createdImageUri: Uri? = null

    suspend fun createUser(
        email: String,
        name: String,
        netId: String,
        skip: Boolean,
        goal: Int,
        imageUri: Uri? = null
    ): Boolean {
        onboardingMutex.withLock {
            try{
                // Resume after a partial failure instead of recreating an account that already exists
                val userFields = createdUserFields?.takeIf { it.netId == netId }
                    ?: createOrRecoverAccount(email, name, netId, imageUri)
                    ?: return false
                val loginResponse = apolloClient.mutation(
                    LoginUserMutation(
                        netId = netId
                    )
                ).execute()
                val id = userFields.id.toIntOrNull()
                if (id == null) {
                    Log.e(TAG, "Failed to set goal: non-numeric user ID '${userFields.id}'")
                    return false
                }
                val loginData = loginResponse.data?.loginUser
                if (loginData?.accessToken == null || loginData.refreshToken == null) {
                    Log.e(TAG, "Login failed after creation: ${loginResponse.errors}")
                    return false
                }
                val accessToken = loginData.accessToken
                val refreshToken = loginData.refreshToken

                if (imageUri != null && imageUri != createdImageUri) {
                    uploadProfilePhoto(id, imageUri, accessToken)
                    createdImageUri = imageUri
                }
                if (!skip) {
                    if (!uploadGoal(id, goal, accessToken)) {
                        return false
                    }
                }
                else {
                    Log.d(TAG, "Skipping goal upload")
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
                createdUserFields = null
                createdImageUri = null
                Log.d(TAG, "User created successfully")
                return true
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Error creating user: $e")
                return false
            }
        }
    }

    // Creates the backend account or recovers one an earlier attempt already created
    private suspend fun createOrRecoverAccount(
        email: String,
        name: String,
        netId: String,
        imageUri: Uri?
    ): UserFields? {
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
        val created = response.data?.createUser?.userFields
        if (!response.hasErrors() && created != null) {
            createdUserFields = created
            createdImageUri = imageUri
            return created
        }
        Log.e(TAG, "Create user failed: ${response.errors}", response.exception)

        val existing = lookUpUserFields(netId).getOrNull() ?: return null
        Log.w(TAG, "Account for this NetID already exists; resuming setup")
        createdUserFields = existing
        // Unknown which photo the existing account has, so the selected one is re-applied
        createdImageUri = null
        return existing
    }

    suspend fun updateProfilePhoto(uri: Uri): Result<Uri> {
        return try {
            val userId = getUserIdFromDataStore()?.toIntOrNull()
                ?: throw IllegalStateException("Missing or invalid user ID")
            Result.success(uploadProfilePhoto(userId, uri))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Failed to update profile photo", e)
            Result.failure(e)
        }
    }

    private suspend fun uploadProfilePhoto(
        userId: Int,
        uri: Uri,
        manualToken: String? = null
    ): Uri {
        val encodedImage = encodeProfileImage(uri)
        val call = apolloClient.mutation(
            UpdateProfilePhotoMutation(userId = userId, encodedImage = encodedImage)
        )
        if (manualToken != null) {
            call.addHttpHeader("Authorization", "Bearer $manualToken")
        }
        val response = call.execute()
        check(!response.hasErrors()) { "Profile photo update failed" }
        val updatedUser = response.data?.editUser
            ?: throw IllegalStateException("Profile photo update returned no user")
        val imageUrl = updatedUser.encodedImage
        check(!imageUrl.isNullOrBlank()) { "Updated profile photo URL missing" }
        return imageUrl.toUri()
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
            ByteArrayOutputStream().use { outputStream ->
                check(bitmap.compress(Bitmap.CompressFormat.JPEG, 20, outputStream)) {
                    "Failed to compress profile image"
                }
                Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
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
                    Log.e(TAG, "Failed to log in: non-numeric user ID resulting in null '${userInfo.id}'")
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
                Log.e(TAG, "Login failed: Missing tokens or user info;  ${loginResponse.errors}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error logging in: $e")
            false
        }
    }

    suspend fun uploadGoal(id:Int?, goal: Int, manualToken: String? = null): Boolean {
        if (id == null) {
            Log.e(TAG, "Failed to set goal: non-numeric user ID '$id'")
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
        val result = call.execute().toResult()
        val data = result.getOrElse { error ->
            Log.e(TAG, "Failed to set goal", error)
            return false
        }
        if (data.setWorkoutGoals == null) {
            Log.e(TAG, "Goal update returned no updated user")
            return false
        }
        Log.d(TAG, "Goal set successfully: $goal")
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
            Log.e(TAG, "Error getting user by netId: $e")
            return null
        }
    }

    fun hasFirebaseUser(): Boolean {
        return firebaseAuth.currentUser != null
    }

    fun getFirebaseUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    // Failure means the check couldn't be completed (e.g. offline)
    suspend fun hasUser(netId: String): Result<Boolean> {
        if (!hasFirebaseUser()) return Result.success(false)
        return lookUpUserFields(netId).map { it != null }
    }

    // Helps check if an account exists or not
    private suspend fun lookUpUserFields(netId: String): Result<UserFields?> {
        return try {
            val response = apolloClient.query(GetUserByNetIdQuery(netId = netId)).execute()
            val fields = response.data?.getUserByNetId?.firstOrNull()?.userFields
            val notFound = response.exception == null && (
                response.errors.orEmpty().any { it.message.contains("does not exist") } ||
                    (!response.hasErrors() && response.data != null)
                )
            when {
                fields != null -> Result.success(fields)
                notFound -> Result.success(null)
                else -> Result.failure(
                    response.exception ?: IllegalStateException("User lookup failed: ${response.errors}")
                )
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signInWithGoogle(idToken: String) {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(firebaseCredential).await()
    }

    // Waits for an in-flight createUser so it can't start a session after this sign-out
    suspend fun signOut() {
        onboardingMutex.withLock {
            createdUserFields = null
            createdImageUri = null
            try {
                firebaseAuth.signOut()
            } catch (e: Exception) {
                Log.e(TAG, "Firebase signout failed: $e")
            } finally {
                sessionManager.logout()
                dataStore.edit { it.clear() }
            }
        }
    }

    suspend fun deleteAccount(): Boolean {
        return try {
            val userId = sessionManager.userId
            if (userId == null) {
                Log.e(TAG, "Delete account failed: No user ID found in session")
                return false
            }

            val firebaseUser = firebaseAuth.currentUser
            val result = apolloClient.mutation(DeleteUserMutation(userId = userId))
                .execute()
                .toResult()
            val data = result.getOrElse { error ->
                Log.e(TAG, "Account deletion request failed", error)
                return false
            }
            if (data.deleteUser == null) {
                Log.e(TAG, "Account deletion returned no deleted user")
                return false
            }

            // Once backend deletion succeeds, local cleanup must survive screen cancellation.
            try {
                firebaseUser?.delete()?.await()
                Log.d(TAG, "Firebase account deleted successfully")
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Firebase delete failed (User may need to re-login): $e")
            } finally {
                withContext(NonCancellable) {
                    // If delete() failed, Firebase would stay signed in as the deleted user
                    try {
                        firebaseAuth.signOut()
                    } catch (e: Exception) {
                        Log.e(TAG, "Firebase signout after deletion failed: $e")
                    }
                    sessionManager.logout()
                    dataStore.edit { it.clear() }
                }
            }

            Log.d(TAG, "Successfully signed out")
            true
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting account: $e")
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
