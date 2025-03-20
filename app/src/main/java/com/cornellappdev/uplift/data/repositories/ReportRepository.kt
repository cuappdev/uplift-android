package com.cornellappdev.uplift.data.repositories

import com.apollographql.apollo.ApolloClient
import com.cornellappdev.uplift.CreateReportMutation
import com.cornellappdev.uplift.data.mappers.toResult
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A client for interacting with the report API.
 * @param apolloClient The Apollo client to use for making requests.
 */
@Singleton
class ReportRepository @Inject constructor(
    private val apolloClient: ApolloClient
) {
    /**
     * @param createdAt The time the report was created.
     * @param description The description of the report.
     * @param gymId The ID of the gym the report is about.
     * @param issue The issue the report is about.
     */
    suspend fun createReport(
        createdAt: String, description: String, gymId: Int, issue: String
    ): Result<CreateReportMutation.Data> {
        return apolloClient.mutation(
            CreateReportMutation(
                createdAt = createdAt,
                description = description,
                gymId = gymId,
                issue = issue
            )
        ).execute().toResult()
    }
}
