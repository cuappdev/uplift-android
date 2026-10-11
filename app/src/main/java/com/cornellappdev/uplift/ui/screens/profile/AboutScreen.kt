package com.cornellappdev.uplift.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.components.general.UpliftTopBarWithBack
import com.cornellappdev.uplift.ui.components.reporting.ReportDropdown
import com.cornellappdev.uplift.ui.theme.AppTextStyles
import com.cornellappdev.uplift.ui.viewmodels.profile.AboutUiState
import com.cornellappdev.uplift.ui.viewmodels.profile.AboutViewModel
import com.cornellappdev.uplift.ui.viewmodels.profile.TeamMember
import com.cornellappdev.uplift.util.GRAY03

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
                )
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.appdev_rocket),
                    contentDescription = "appdev rocket",
                    modifier = Modifier
                        .size(95.6.dp)
                )

                Text(
                    text = "Designed and developed by:",
                    style = AppTextStyles.LabelNormal,
                )

                Text(
                    text =  "CornellAppDev",
                    style = AppTextStyles.BodySemibold,
                    fontSize = 28.96.sp,
                   )

            }

            ReportDropdown(
                title = "Semester",
                selectedOption = uiState.selectedSemester,
                onSelect = onSemesterSelected,
                options = uiState.semesters,
                // TODO: Implement Error handling soon?
                onErrorStateChange = {}
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                contentPadding = PaddingValues(bottom = 32.dp),
                modifier = Modifier.weight(1f)
            ) {
                uiState.sections.forEachIndexed { index, section ->

                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Text(
                            text = section.semester,
                            style = AppTextStyles.BodySemibold,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = if (index == 0) 0.dp else 16.dp)
                        )
                    }

                    items(section.members) { member ->
                        TeamMemberItem(member)
                    }
                }

            }

        }
    }
}

@Composable
private fun TeamMemberItem(member: TeamMember) {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = member.imageRes),
            contentDescription = member.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(64.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = member.name,
                style = AppTextStyles.BodySemibold
            )
            Text(
                text = member.role,
                style = AppTextStyles.LabelBig,
                color = GRAY03
            )
        }
    }
}

@Preview
@Composable
private fun AboutScreenPreview() {
    AboutScreenContent(uiState = AboutUiState(), onBackClick = {}, onSemesterSelected = {})
}