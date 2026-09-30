package com.cornellappdev.uplift.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.uplift.ui.components.general.UpliftTopBarWithBack

@Composable
fun AboutScreen(
    onBackClick: () -> Unit
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
                Text("About Page here")
            }
    }
}

@Preview
@Composable
private fun AboutScreenPreview() {
    AboutScreen(onBackClick = {})
}