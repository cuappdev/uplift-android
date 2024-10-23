package com.cornellappdev.uplift.ui.components.reporting

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.sharp.KeyboardArrowDown
import androidx.compose.material.icons.sharp.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.GRAY03
import com.cornellappdev.uplift.util.GRAY04
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.montserratFamily

/**
 * ReportDropdown is a composable that displays a label and a dropdown menu for the user to select
 * an option.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportDropdown(
    title: String,
    selectedOption: String = "Choose an option ...",
    options: List<String> = emptyList(),
    onSelect: (String) -> Unit,
    errorState: Boolean = false,
    onErrorStateChange: (Boolean) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (errorState && !expanded) 1f else 0f,
        label = "error text"
    )

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
                    cursorColor = PRIMARY_BLACK,

                    ),
                textStyle = TextStyle(
                    color = GRAY04,
                    fontSize = 14.sp,
                    fontFamily = montserratFamily,
                    fontWeight = FontWeight.Medium
                ),
                shape = RoundedCornerShape(8.dp),
                trailingIcon = {
                    if (expanded) {
                        Icon(
                            Icons.Filled.KeyboardArrowUp,
                            contentDescription = "Close dropdown",
                            tint = GRAY03
                        )
                    } else {
                        Icon(
                            Icons.Filled.KeyboardArrowDown,
                            contentDescription = "Open dropdown",
                            tint = GRAY03
                        )
                    }
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
            Text(
                text = "This is a required field.",
                color = Color.Red,
                fontSize = 12.sp,
                fontFamily = montserratFamily,
                modifier = Modifier
                    .padding(top = 54.dp, start = 6.dp)
                    .alpha(alpha)
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
                                .padding(horizontal = 16.dp)
                                .padding(vertical = 8.dp)
                                .clickable {
                                    onSelect(option)
                                    onErrorStateChange(false)
                                    expanded = false
                                }
                        )
                    }
                }
            }
        }

        // Add a Spacer to create the necessary gap when the dropdown is expanded
        Spacer(modifier = Modifier.height(if (expanded) 24.dp else 0.dp))
    }
}