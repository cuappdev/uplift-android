package com.cornellappdev.uplift.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.cornellappdev.uplift.domain.ReportClient
import com.cornellappdev.uplift.nav.RootNavigationRepository
import com.cornellappdev.uplift.ui.UpliftRootRoute
import com.cornellappdev.uplift.util.HELEN_NEWMAN_ID
import com.cornellappdev.uplift.util.MORRISON_ID
import com.cornellappdev.uplift.util.NOYES_ID
import com.cornellappdev.uplift.util.TEAGLE_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val rootNavigationRepository: RootNavigationRepository,
    private val reportClient: ReportClient
) : ViewModel() {
    suspend fun createReport(
        issue: String,
        gym: String,
        description: String,
        userId: Int
    ) {
        val reportSuccess = reportClient.createReport(
            createdAt = LocalDateTime.now().toString(),
            description = description,
            gymId = mapGymToId(gym),
            issue = formatIssue(issue),
            userId = userId
        )
        if (reportSuccess) {
            rootNavigationRepository.navigate(UpliftRootRoute.ReportSuccess)
        }
    }

    private fun mapGymToId(gym: String): Int {
        return when (gym) {
            "Helen Newman" -> HELEN_NEWMAN_ID
            "Noyes" -> NOYES_ID
            "Morrison" -> MORRISON_ID
            else -> TEAGLE_ID
        }
    }

    private fun formatIssue(issue: String): String {
        return when (issue) {
            "Inaccurate equipment" -> "INACCURATE_EQUIPMENT"
            "Incorrect hours" -> "INCORRECT_HOURS"
            "Inaccurate description" -> "INACCURATE_DESCRIPTION"
            "Wait times not updated" -> "WAIT_TIMES_NOT_UPDATED"
            else -> "OTHER"
        }
    }

    fun navigateToHome() {
        rootNavigationRepository.navigate(UpliftRootRoute.Home)
    }

    fun navigateToReport() {
        rootNavigationRepository.navigate(UpliftRootRoute.ReportIssue)
    }

}

