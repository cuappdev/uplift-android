package com.cornellappdev.uplift.ui.viewmodels.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornellappdev.uplift.data.repositories.ReportRepository
import com.cornellappdev.uplift.ui.nav.RootNavigationRepository
import com.cornellappdev.uplift.ui.UpliftRootRoute
import com.cornellappdev.uplift.util.HELEN_NEWMAN_ID
import com.cornellappdev.uplift.util.MORRISON_ID
import com.cornellappdev.uplift.util.NOYES_ID
import com.cornellappdev.uplift.util.TEAGLE_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val rootNavigationRepository: RootNavigationRepository,
    private val reportRepository: ReportRepository
) : ViewModel() {
    private val _reportButtonEnabled = MutableStateFlow(true)
    val reportButtonEnabled = _reportButtonEnabled

    fun createReport(
        issue: String,
        gym: String,
        description: String,
    ) {
        viewModelScope.launch {
            _reportButtonEnabled.value = false
            val reportSuccess = reportRepository.createReport(
                createdAt = LocalDateTime.now().toString(),
                description = description,
                gymId = mapGymToId(gym),
                issue = formatIssue(issue),
            )
            //TODO: Consider adding error handling for the case when reportSuccess is not true, maybe a toast?
            if (reportSuccess.isSuccess) {
                rootNavigationRepository.navigate(UpliftRootRoute.ReportSuccess)
            }
            _reportButtonEnabled.value = true
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

}

