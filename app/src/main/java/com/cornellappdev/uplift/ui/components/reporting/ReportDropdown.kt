package com.cornellappdev.uplift.ui.components.reporting

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cornellappdev.uplift.ui.components.general.UpliftDropdown

/**
 * ReportDropdown is a specialized version of [UpliftDropdown] configured
 * with error validation for reporting errors.
 */
@Composable
fun ReportDropdown(
    title: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
    selectedOption: String = "Choose an option ...",
    options: List<String> = emptyList(),
    errorState: Boolean = false,
    onErrorStateChange: (Boolean) -> Unit
) {
    UpliftDropdown(
        title = title,
        onSelect = { option ->
            onSelect(option)
            onErrorStateChange(false)
        },
        modifier = modifier,
        selectedOption = selectedOption,
        options = options,
        errorState = errorState,
        errorMessage = "This is a required field.",
    )
}
