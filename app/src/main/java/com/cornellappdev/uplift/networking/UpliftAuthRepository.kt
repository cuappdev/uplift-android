package com.cornellappdev.uplift.networking

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.cornellappdev.uplift.CreateUserMutation
import com.google.firebase.firestore.auth.User
//import com.cornellappdev.uplift.android.model.api.CreateUserBody
//import com.cornellappdev.uplift.android.model.api.LoginBody
//import com.cornellappdev.uplift.android.model.api.RetrofitInstance
//import com.cornellappdev.resell.android.model.api.User
//import com.cornellappdev.resell.android.model.core.UserInfoRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpliftAuthRepository @Inject constructor(
  private val userInfoRepository: UserInfoRepository
) {
  private val apolloClient = ApolloClient.Builder()
//        .serverUrl("https://uplift-backend.cornellappdev.com/graphql")
    .serverUrl("https://uplift-dev.cornellappdev.com/graphql")
    .build()

  suspend fun createUser(email: String, name: String, netid: String): CreateUserMutation.Data {
    val response = apolloClient.mutation(
      CreateUserMutation(
        email = email,
        name = name,
        netId = netid,
      )
    ).execute()

    return response.data!!
  }

  /**
   * Hits the Resell backend to check if our auth session is still valid. If not,
   * it will attempt to re-authenticate. Requires that the user's Id has been set in
   * `userInfoRepository`.
   *
   * At the end, with a successful auth session, it will store the new access token, and
   * proceed, such that the access token can be accessed by `userInfoRepository`.
   *
   * If for any reason the auth fails, an exception will be thrown.
   */
    suspend fun authenticate() {
        // Allowed by precondition
        val userId = userInfoRepository.getUserId()!!

        //val response = retrofitInstance.loginApi.getSession(userId)
        //var session = response.sessions[0]

//        if (!session.active) {
//            Log.d("UpliftAuthRepository", "Session is not active. Refreshing...")
//            //session = retrofitInstance.loginApi.refreshSession(session.refreshToken).session
//        }
        //userInfoRepository.storeAccessToken(session.accessToken)}
    }

}