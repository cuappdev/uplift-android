package com.cornellappdev.uplift.ui.components.reminders.classreminder

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.util.GRAY02
import com.cornellappdev.uplift.util.GRAY04
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.montserratFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassReminderInput(
    label: String,
    placeholder: String,
    textValue: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    errorState: Boolean = false,
    onErrorStateChange: (Boolean) -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val contentColor = if (enabled) PRIMARY_BLACK else GRAY02
    val errorColor = if (errorState) Color.Red else contentColor

    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = textValue,
            onValueChange = onValueChange,
            enabled = enabled,
            modifier = Modifier
                .width(40.dp)
                .height(32.dp)
                .border(
                    width = 1.dp,
                    color = errorColor,
                    shape = RoundedCornerShape(8.dp)
                ),
            textStyle = TextStyle(
                color = contentColor,
                fontSize = 14.sp,
                fontFamily = montserratFamily,
                textAlign = TextAlign.Center
            ),
            singleLine = true,
            interactionSource = interactionSource,
            cursorBrush = SolidColor(contentColor),
            decorationBox = { innerTextField ->
                OutlinedTextFieldDefaults.DecorationBox(
                    value = textValue,
                    innerTextField = innerTextField,
                    enabled = enabled,
                    singleLine = true,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = interactionSource,
                    placeholder = {
                        if (!isFocused) {
                            Text(
                                text = placeholder,
                                color = if (enabled) GRAY04 else GRAY02,
                                fontSize = 14.sp,
                                fontFamily = montserratFamily,
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp),
                    container = {}
                )
            }
        )

        Text(
            text = label,
            color = errorColor,
            fontSize = 16.sp,
            fontFamily = montserratFamily,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview
@Composable
private fun ClassReminderInputPreview() {
    var userInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ClassReminderInput(
            label = "mins",
            placeholder = "0",
            textValue = userInput,
            onValueChange = { userInput = it },
            enabled = true,
            errorState = false,
            onErrorStateChange = {}
        )
    }

}