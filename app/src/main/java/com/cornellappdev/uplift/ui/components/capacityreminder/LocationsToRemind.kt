package com.cornellappdev.uplift.ui.components.capacityreminder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.util.GRAY03
import com.cornellappdev.uplift.util.montserratFamily

/**
 * Section with locations to remind the user to check the capacity.
 */
@Composable
fun LocationsToRemind(
    initialSelectedGyms: Set<String> = emptySet(),
    onGymSelected: (Set<String>) -> Unit
) {
    val topRow = listOf("Teagle Up", "Teagle Down", "Helen Newman")
    val bottomRow = listOf("Toni Morrison", "Noyes")

    var selectedGyms by remember { mutableStateOf(initialSelectedGyms) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(108.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "LOCATIONS TO REMIND",
            fontFamily = montserratFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = GRAY03
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(76.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            GymRow(
                gyms = topRow,
                selectedGyms = selectedGyms,
                onGymSelected = {
                    selectedGyms = it
                    onGymSelected(it)
                }
            )
            GymRow(
                gyms = bottomRow,
                selectedGyms = selectedGyms,
                onGymSelected = {
                    selectedGyms = it
                    onGymSelected(it)
                }
            )
        }
    }
}

/**
 * Component for row of gym tags.
 */
@Composable
private fun GymRow(
    gyms: List<String>,
    selectedGyms: Set<String>,
    onGymSelected: (Set<String>) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        gyms.forEach {
            GymTag(
                gymName = it, isSelected = selectedGyms.contains(it)
            ) {
                val newSelectedGyms = if (selectedGyms.contains(it)) {
                    selectedGyms.minus(it)
                } else {
                    selectedGyms.plus(it)
                }
                onGymSelected(newSelectedGyms)
            }
        }
    }
}