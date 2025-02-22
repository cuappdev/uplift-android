package com.cornellappdev.uplift.data.clients

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.cornellappdev.uplift.CreateReportMutation
import com.cornellappdev.uplift.domain.report.ReportClient

/**
 * A client for interacting with the report API.
 * @param apolloClient The Apollo client to use for making requests.
 */
class ApolloReportClient(
    private val apolloClient: ApolloClient
) : ReportClient {
    /**
     * @param createdAt The time the report was created.
     * @param description The description of the report.
     * @param gymId The ID of the gym the report is about.
     * @param issue The issue the report is about.
     * @return True if the report was created successfully, false otherwise.
     */
    override suspend fun createReport(
        createdAt: String, description: String, gymId: Int, issue: String
    ): Boolean {
        val response = apolloClient.mutation(
            CreateReportMutation(
                createdAt = createdAt,
                description = description,
                gymId = gymId,
                issue = issue
            )
        ).execute()

        return when {
            response.hasErrors() -> {
                Log.w("ApolloReportClient", "Error creating report: ${response.errors}")
                false
            }

            else -> {
                Log.d("ApolloReportClient", "Report created successfully" + response.data)
                true
            }
        }
    }
}
