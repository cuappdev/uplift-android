package com.cornellappdev.uplift.ui.components.reporting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

@Preview(showBackground = true)
@Composable
private fun ReportDropdownPreview() {
    var selectedOption by remember { mutableStateOf("Choose an option ...") }
    var errorState by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(16.dp)
    ) {
        ReportDropdown(
            title = "Test Dropdown",
            onSelect = { selectedOption = it },
            selectedOption = selectedOption,
            options = listOf("Option 1", "Option 2", "Option 3"),
            errorState = errorState,
            onErrorStateChange = { errorState = it }
        )
    }
}
