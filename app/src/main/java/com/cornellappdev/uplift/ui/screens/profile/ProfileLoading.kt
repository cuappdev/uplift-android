package com.cornellappdev.uplift.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.unit.dp
import com.cornellappdev.uplift.ui.components.general.LoadingPlaceholder
import com.valentinilk.shimmer.Shimmer

@Composable
internal fun ProfileLoading(shimmer: Shimmer) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 24.dp)
            .clearAndSetSemantics {
                contentDescription = "Loading profile"
                progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LoadingPlaceholder(shimmer, Modifier.size(98.dp), CircleShape)

            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                LoadingPlaceholder(shimmer, Modifier.fillMaxWidth(0.8f).height(24.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(36.dp)) {
                    repeat(2) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            LoadingPlaceholder(shimmer, Modifier.width(24.dp).height(16.dp))
                            LoadingPlaceholder(shimmer, Modifier.width(60.dp).height(12.dp))
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        LoadingPlaceholder(shimmer, Modifier.width(100.dp).height(24.dp))
        Spacer(Modifier.height(12.dp))
        LoadingPlaceholder(shimmer, Modifier.align(Alignment.CenterHorizontally).width(250.dp).height(132.dp))
        Spacer(Modifier.height(16.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            repeat(7) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    LoadingPlaceholder(shimmer, Modifier.width(20.dp).height(12.dp))
                    Spacer(Modifier.height(4.dp))
                    LoadingPlaceholder(shimmer, Modifier.size(24.dp), CircleShape)
                    Spacer(Modifier.height(4.dp))
                    LoadingPlaceholder(shimmer, Modifier.width(16.dp).height(12.dp))
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Column(Modifier.fillMaxWidth().weight(1f)) {
            LoadingPlaceholder(shimmer, Modifier.width(180.dp).height(24.dp))
            Spacer(Modifier.height(12.dp))
            Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                repeat(3) {
                    Row(
                        modifier = Modifier.fillMaxWidth().height(60.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            LoadingPlaceholder(shimmer, Modifier.width(100.dp).height(12.dp))
                            LoadingPlaceholder(shimmer, Modifier.width(160.dp).height(12.dp))
                        }
                        LoadingPlaceholder(shimmer, Modifier.width(48.dp).height(12.dp))
                    }
                }
            }
        }
    }
}
