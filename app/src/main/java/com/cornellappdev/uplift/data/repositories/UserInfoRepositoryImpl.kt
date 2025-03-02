package com.cornellappdev.uplift.data.repositories;

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.Preferences
import com.apollographql.apollo3.ApolloClient
import com.cornellappdev.uplift.CreateUserMutation
import com.cornellappdev.uplift.GetUserByNetIdQuery
import kotlinx.coroutines.flow.map;
import kotlinx.coroutines.flow.firstOrNull
import com.cornellappdev.uplift.data.models.UserInfo
import com.cornellappdev.uplift.di.PreferencesKeys
import com.cornellappdev.uplift.domain.repositories.UserInfoRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
                storeId(response.data?.createUser?.userFields?.let { storeId(it.id) }.toString())
                storeNetId(netId)
                storeUsername(name)
                storeEmail(email)
                storeSkip(false)
                Log.d("UserInfoRepositoryImpl", "User created successfully" + response.data)
                true
            }
        }
    }

    override suspend fun getUserByNetId(netId: String): UserInfo {
        val response = apolloClient.query(
            GetUserByNetIdQuery(
                netId = netId
            )
        ).execute()
        return UserInfo(
            id = response.data?.getUserByNetId?.get(0)?.userFields?.id ?: "",
            name = response.data?.getUserByNetId?.get(0)?.userFields?.name ?: "",
            email = response.data?.getUserByNetId?.get(0)?.userFields?.email ?: "",
            netId = response.data?.getUserByNetId?.get(0)?.userFields?.netId ?: "",
        )
    }

    override fun hasFirebaseUser(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override suspend fun getFirebaseUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    override suspend fun hasUser(netId: String): Boolean {
        return hasFirebaseUser() && getUserByNetId(netId).id.isNotEmpty()
    }

    override suspend fun signInWithGoogle(idToken: String) {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(firebaseCredential).await()
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }

    override suspend fun getUserInfoFromDataStore(): UserInfo {
        return UserInfo(
            id = getUserIdFromDataStore(),
            name = getUserNameFromDataStore(),
            email = getEmailFromDataStore(),
            netId = getNetIdFromDataStore(),
        )
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

    override suspend fun storeSkip(skip: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SKIP] = skip
        }
    }

    override suspend fun getSkipFromDataStore(): Boolean {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.SKIP]
        }.firstOrNull() ?: false
    }

    override suspend fun getUserIdFromDataStore(): String {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.ID]
        }.firstOrNull() ?: ""
    }

    override suspend fun getUserNameFromDataStore(): String {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.USERNAME]
        }.firstOrNull() ?: ""
    }

    override suspend fun getNetIdFromDataStore(): String {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.NETID]
        }.firstOrNull() ?: ""
    }

    override suspend fun getEmailFromDataStore(): String {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.EMAIL]
        }.firstOrNull() ?: ""
    }

}
