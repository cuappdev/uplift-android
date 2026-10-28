package com.cornellappdev.uplift.ui.viewmodels.reminders

data class ClassRemindersUiState(
    val toggledOn: Boolean = false,
    val selectedToggle: Boolean = false,
    val isLoading: Boolean = false,
    val hasUnsavedChanges: Boolean = false,
    val saveSuccess: Boolean = false,
    val error: String? = null,
    val showUnsavedChangesDialog: Boolean = false
)