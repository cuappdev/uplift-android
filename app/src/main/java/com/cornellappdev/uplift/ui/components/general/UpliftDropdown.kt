package com.cornellappdev.uplift.ui.components.general

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.GRAY03
import com.cornellappdev.uplift.util.GRAY04
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.montserratFamily

/**
 * UpliftDropdown is a reusable composable that displays a label and a dropdown menu
 * for selecting an option, with optional error state handling.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpliftDropdown(
    title: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
    selectedOption: String = "Choose an option ...",
    options: List<String> = emptyList(),
    errorState: Boolean = false,
    errorMessage: String? = null
) {
    var expanded by remember { mutableStateOf(false) }
    val spacerHeight by animateDpAsState(
        targetValue = if (expanded) 44.dp else 0.dp,
        animationSpec = tween(300),
        label = "spacer height"
    )
    val alpha by animateFloatAsState(
        targetValue = if (errorState && !expanded) 1f else 0f,
        label = "error text"
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title,
            color = PRIMARY_BLACK,
            fontSize = 16.sp,
            fontFamily = montserratFamily,
            fontWeight = FontWeight.Bold
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            TextField(
                value = selectedOption,
                onValueChange = {},
                readOnly = true,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = GRAY01,
                    focusedContainerColor = GRAY01,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    cursorColor = PRIMARY_BLACK
                ),
                textStyle = TextStyle(
                    color = GRAY04,
                    fontSize = 14.sp,
                    fontFamily = montserratFamily,
                    fontWeight = FontWeight.Medium
                ),
                shape = RoundedCornerShape(8.dp),
                trailingIcon = {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        tint = GRAY03
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
                    .height(48.dp)
                    .border(
                        if (errorState && !expanded) 1.dp else 0.dp,
                        if (errorState && !expanded) Color.Red else Color.Transparent,
                        RoundedCornerShape(8.dp)
                    )
            )

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(animationSpec = tween(300)) + fadeIn(),
                exit = shrinkVertically(animationSpec = tween(300)) + fadeOut(),
                modifier = Modifier
                    .offset(y = 44.dp)
                    .padding(bottom = 8.dp)
                    .clip(RoundedCornerShape(0.dp, 0.dp, 8.dp, 8.dp))
            ) {
                Column(
                    modifier = Modifier
                        .background(GRAY01)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(0.dp, 0.dp, 8.dp, 8.dp))
                ) {
                    options.forEach { option ->
                        Text(
                            text = option,
                            color = PRIMARY_BLACK,
                            fontSize = 14.sp,
                            fontFamily = montserratFamily,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .clickable {
                                    onSelect(option)
                                    expanded = false
                                }
                        )
                    }
                }
            }
        }

        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 12.sp,
                fontFamily = montserratFamily,
                modifier = Modifier
                    .padding(start = 6.dp)
                    .alpha(alpha)
            )
        }

        // Add a Spacer to create the necessary gap when the dropdown is expanded
        Spacer(modifier = Modifier.height(spacerHeight))
    }
}

@Preview(showBackground = true)
@Composable
private fun UpliftDropdownPreview() {
    var selectedOption by remember { mutableStateOf("Choose an option ...") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(16.dp)
    ) {
        UpliftDropdown(
            title = "Test Dropdown",
            onSelect = {selectedOption = it},
            selectedOption = selectedOption,
            options = listOf("Option 1", "Option 2", "Option 3"),
            errorState = false
        )
    }
}
