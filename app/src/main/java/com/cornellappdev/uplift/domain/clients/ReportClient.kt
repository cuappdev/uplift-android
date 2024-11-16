package com.cornellappdev.uplift.domain.clients

/**
 * A client for interacting with the report API.
 */
interface ReportClient {
    /**
     * Creates a report.
     *
     * @param createdAt The time the report was created.
     * @param description The description of the report.
     * @param gymId The ID of the gym the report is about.
     * @param issue The issue the report is about.
     * @param userId The ID of the user creating the report.
     * @return True if the report was created successfully, false otherwise.
     */
    suspend fun createReport(
        createdAt: String,
        description: String,
        gymId: Int,
        issue: String,
        userId: Int
    ) : Boolean
}