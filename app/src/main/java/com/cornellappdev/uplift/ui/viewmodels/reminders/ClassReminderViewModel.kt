package com.cornellappdev.uplift.ui.viewmodels.reminders

import com.cornellappdev.uplift.ui.UpliftRootRoute
import com.cornellappdev.uplift.ui.nav.RootNavigationRepository
import com.cornellappdev.uplift.ui.viewmodels.UpliftViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


data class ClassReminderUiState(
    val toggledOn: Boolean = false,
    val selectedToggle: Boolean = false,
    val isLoading: Boolean = false,
    val saveSuccess: Boolean = false,
    val error: String? = null,
    val showUnsavedChangesDialog: Boolean = false
)

@HiltViewModel
class ClassReminderViewModel @Inject constructor(
//    private val classRemindersRepository: ClassRemindersRepository,
//    private val dataStoreRepository: DatastoreRepository,
    private val rootNavigationRepository: RootNavigationRepository
) : UpliftViewModel<ClassReminderUiState>(ClassReminderUiState()) {

    fun onBack() {
        rootNavigationRepository.navigateUp()
    }

    fun setToggle(toggledOn: Boolean) {
        applyMutation {
            copy(
                toggledOn = toggledOn,
                saveSuccess = false
            )
        }
    }

}