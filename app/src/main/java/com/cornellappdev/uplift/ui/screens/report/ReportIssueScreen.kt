package com.cornellappdev.uplift.ui.screens.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cornellappdev.uplift.ui.components.general.UpliftTopBarWithBack
import com.cornellappdev.uplift.ui.components.reporting.ReportDescription
import com.cornellappdev.uplift.ui.components.reporting.ReportDropdown
import kotlinx.coroutines.launch
import com.cornellappdev.uplift.ui.components.general.UpliftButton
import com.cornellappdev.uplift.ui.viewmodels.report.ReportViewModel

/**
 * ReportIssueScreen is a composable that displays the report issue screen where users can report
 * an issue with the app.
 */
@Composable
fun ReportIssueScreen(
    reportViewModel: ReportViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    var selectedIssue by remember { mutableStateOf("Choose an option ...") }
    var selectedGym by remember { mutableStateOf("Choose an option ...") }
    var description by remember { mutableStateOf("") }
    var errorStateIssue by remember { mutableStateOf(false) }
    var errorStateGym by remember { mutableStateOf(false) }
    val enabled = reportViewModel.reportButtonEnabled.collectAsState().value
    Scaffold(topBar = {
        UpliftTopBarWithBack(
            title = "Report an issue",
            onBackClick = onBack,
            withBack = true
        )
    }) { padding ->
        Column(
            modifier = Modifier
                .padding(
                    top = padding.calculateTopPadding(),
                )
                .fillMaxSize()
                .background(color = Color.White)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, start = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                ReportDropdown(
                    title = "What was the issue?",
                    selectedOption = selectedIssue,
                    onSelect = { selectedIssue = it },
                    options = listOf(
                        "Inaccurate equipment",
                        "Incorrect hours",
                        "Inaccurate description",
                        "Wait times not updated",
                        "Other"
                    ),
                    errorState = errorStateIssue,
                    onErrorStateChange = { errorStateIssue = it }
                )
                ReportDropdown(
                    title = "Which gym does it concern?",
                    selectedOption = selectedGym,
                    onSelect = { selectedGym = it },
                    options = listOf(
                        "Morrison",
                        "Teagle",
                        "Helen Newman",
                        "Noyes",
                        "Other"
                    ),
                    errorState = errorStateGym,
                    onErrorStateChange = { errorStateGym = it }
                )
                ReportDescription(
                    label = "Describe what's wrong for us.",
                    placeholder = "What happened?",
                    textValue = description,
                    onValueChange = { description = it }
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 68.dp, bottom = 77.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                UpliftButton(
                    onClick = {
                        errorStateIssue = selectedIssue == "Choose an option ..."
                        errorStateGym = selectedGym == "Choose an option ..."
                        if (!errorStateIssue && !errorStateGym) {
                            reportViewModel.createReport(
                                selectedIssue,
                                selectedGym,
                                description,
                            )
                        }
                    },
                    enabled = enabled,
                    width = 106.dp,
                    height = 42.dp
                )
            }


        }
    }
}


@Preview(showBackground = true)
@Composable
private fun ReportIssueScreenPreview() {
    ReportIssueScreen(
        onBack = { }
    )
}
