package com.cornellappdev.uplift.ui.viewmodels.gyms

import androidx.lifecycle.viewModelScope
import com.cornellappdev.uplift.data.models.ApiResponse
import com.cornellappdev.uplift.data.models.TimeOfDay
import com.cornellappdev.uplift.data.models.gymdetail.UpliftGym
import com.cornellappdev.uplift.data.repositories.UpliftApiRepository
import com.cornellappdev.uplift.domain.repositories.UserInfoRepository
import com.cornellappdev.uplift.ui.viewmodels.UpliftViewModel
import com.cornellappdev.uplift.util.getSystemTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val title: String,
    val gyms: List<UpliftGym>,
    val gymsLoading: Boolean = false,
    val gymsError: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val upliftApiRepository: UpliftApiRepository,
    private val userInfoRepository: UserInfoRepository
) : UpliftViewModel<HomeUiState>(HomeUiState("", emptyList())) {

    init {
        observeGyms()
    }

    private fun observeGyms() = viewModelScope.launch {
        applyMutation { copy(gymsLoading = true) }
        val title = getHomeTitle()
        upliftApiRepository.gymApiFlow.collect { apiResponse ->
            when (apiResponse) {
                is ApiResponse.Success -> {
                    applyMutation {
                        copy(
                            title = title,
                            gyms = apiResponse.data,
                            gymsLoading = false,
                            gymsError = false
                        )
                    }
                }

                is ApiResponse.Loading -> {
                    applyMutation {
                        copy(gymsLoading = true)
                    }
                }

                is ApiResponse.Error -> {
                    applyMutation {
                        copy(gymsLoading = false, gymsError = true)
                    }
                }
            }
        }
    }

    private suspend fun getHomeTitle(): String {
        var title = getHomeDefaultTitleText()
        if (userInfoRepository.hasFirebaseUser()) {
            val name = userInfoRepository.getFirebaseUser()?.displayName ?: ""
            val firstName = name.split(" ")[0]
            if (firstName.isNotEmpty()) {
                title = "Welcome, $firstName!"
            }
        }
        return title
    }

    /** Call UpliftApiRepository to reload the data. */
    fun reload() {
        upliftApiRepository.reload()
    }

    /** Returns the title text the top bar should display for the home page. */
    private fun getHomeDefaultTitleText(): String {
        val now = getSystemTime()
        return if (now.compareTo(TimeOfDay(4, 0, true)) >= 0 && now.compareTo(
                TimeOfDay(12, 0, false)
            ) < 0
        ) "Good Morning!"
        // 12 PM to 6 PM
        else if (now.compareTo(TimeOfDay(12, 0, false)) >= 0 && now.compareTo(
                TimeOfDay(6, 0, false)
            ) < 0
        ) "Good Afternoon!"
        // 6 PM to 4 AM
        else "Good Evening!"
    }
}
