package com.cornellappdev.uplift.data.repositories

import com.apollographql.apollo3.ApolloClient
import com.cornellappdev.uplift.CreateUserMutation
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpliftAuthRepository @Inject constructor(
  private val userInfoRepository: UserInfoRepository,
  private val apolloClient: ApolloClient
) {

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
   * Hits the Uplift backend to check if our auth session is still valid. If not,
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

        // TODO: Implement this
    }

}