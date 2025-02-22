package com.cornellappdev.uplift.domain.report

class CreateReportUseCase(
    private val reportClient: ReportClient
) {
    suspend fun execute(
        createdAt: String,
        description: String,
        gymId: Int,
        issue: String,
    ): Boolean {
        return reportClient.createReport(createdAt, description, gymId, issue)
    }
}
