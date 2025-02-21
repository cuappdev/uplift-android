package com.cornellappdev.uplift.data.repositories;

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.Preferences
import com.apollographql.apollo3.ApolloClient
import com.cornellappdev.uplift.CreateUserMutation
import kotlinx.coroutines.flow.map;
import kotlinx.coroutines.flow.firstOrNull
import com.cornellappdev.uplift.data.models.UserInfo
import com.cornellappdev.uplift.domain.repositories.UserInfoRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

@Singleton
class UserInfoRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val apolloClient: ApolloClient,
    private val dataStore: DataStore<Preferences>,
) : UserInfoRepository {

    override suspend fun createUser(email: String, name: String, netId: String): Boolean {
        val response = apolloClient.mutation(
            CreateUserMutation(
                email = email,
                name = name,
                netId = netId,
            )
        ).execute()

        return when {
            response.hasErrors() -> {
                Log.w("UserInfoRepositoryImpl", "Error creating user: ${response.errors}")
                false
            }

            else -> {
                // TODO(Add store method for id as well)
                storeNetId(netId)
                storeUsername(name)
                storeEmail(email)
                Log.d("UserInfoRepositoryImpl", "User created successfully" + response.data)
                true
            }
        }
    }

    override suspend fun getUserInfo(): UserInfo {
        return UserInfo(
            id = getUserId() ?: "",
            name = getUsername() ?: "",
            email = getEmail() ?: "",
            netId = getNetId() ?: "",
        )
    }

    override suspend fun hasUser(): Boolean {
        return firebaseAuth.currentUser != null && getNetId() != null
    }

    override suspend fun signInWithGoogle(idToken: String) {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(firebaseCredential).await()
    }

    override suspend fun signOut() {
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

    override suspend fun getUserId(): String? {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.ID]
        }.firstOrNull()
    }

    override suspend fun getUsername(): String? {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.USERNAME]
        }.firstOrNull()
    }

    override suspend fun getNetId(): String? {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.NETID]
        }.firstOrNull()
    }

    override suspend fun getEmail(): String? {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.EMAIL]
        }.firstOrNull()
    }

}
