package com.cornellappdev.uplift.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cornellappdev.uplift.ui.components.general.UpliftTopBarWithBack
import com.cornellappdev.uplift.ui.components.reporting.ReportDropdown
import com.cornellappdev.uplift.ui.viewmodels.profile.AboutUiState
import com.cornellappdev.uplift.ui.viewmodels.profile.AboutViewModel

@Composable
fun AboutScreen(
    onBackClick: () -> Unit,
    aboutViewModel: AboutViewModel = hiltViewModel()
) {
    val uiState = aboutViewModel.collectUiStateValue()
    AboutScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onSemesterSelected = aboutViewModel::onSemesterSelected
    )
    // TODO: Implement error handling
    var AboutErrorState by remember { mutableStateOf(false) }
}

@Composable
private fun AboutScreenContent(
    uiState: AboutUiState,
    onBackClick: () -> Unit,
    onSemesterSelected: (String) -> Unit
) {
    Scaffold(topBar = {
        UpliftTopBarWithBack(title = "About Uplift", onBackClick = onBackClick)
    }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(
                    top = padding.calculateTopPadding() + 24.dp,
                    start = 24.dp,
                    end = 24.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        )
        {
            ReportDropdown(
                title = "All Semesters",
                selectedOption = uiState.selectedSemester,
                onSelect = onSemesterSelected,
                options = uiState.semesters,
                // TODO: Implement Error handling soon?
                onErrorStateChange = {}
            )
            Text("Semester selected: ${uiState.selectedSemester}")
            uiState.members.forEach { Text("${it.name} - ${it.role}") }
        }
    }
}

@Preview
@Composable
private fun AboutScreenPreview() {
    AboutScreenContent(uiState = AboutUiState(), onBackClick = {}, onSemesterSelected = {})
}