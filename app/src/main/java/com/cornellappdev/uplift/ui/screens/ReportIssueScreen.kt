package com.cornellappdev.uplift.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.ui.components.general.UpliftTopBarWithBack
import com.cornellappdev.uplift.ui.components.reporting.ReportDescription
import com.cornellappdev.uplift.ui.components.reporting.SubmitButton
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.montserratFamily

@Composable
fun ReportIssueScreen() {
    var description by remember { mutableStateOf("") }

    Scaffold(topBar = {
        UpliftTopBarWithBack(
            title = "Report an issue",
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
            ){
                ReportDropdown(
                    title = "What was the issue?"
                )
                ReportDropdown(
                    title = "Which gym does it concern?"
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
                    .padding(bottom = 77.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SubmitButton(
                    onSubmit = { }
                )
            }


        }
    }
}

@Composable
fun ReportDropdown(
    title: String,
//    options: List<String>,
//    selectedOption: String,
//    onOptionSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = title,
            color = PRIMARY_BLACK,
            fontSize = 16.sp,
            fontFamily = montserratFamily,
            fontWeight = FontWeight.Bold
        )

    }

}

@Preview(showBackground = true)
@Composable
private fun ReportIssueScreenPreview() {
    ReportIssueScreen()
}