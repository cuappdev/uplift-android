package com.cornellappdev.uplift.ui.components.reporting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.montserratFamily

@Composable
fun ReportDescription(
    label : String,
    placeholder: String,
    textValue : String,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = label,
            color = PRIMARY_BLACK,
            fontSize = 16.sp,
            fontFamily = montserratFamily,
            fontWeight = FontWeight.Bold
        )
        TextField(
            value = textValue,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    color = PRIMARY_BLACK,
                    fontSize = 14.sp,
                    fontFamily = montserratFamily
                )
            },
            textStyle = TextStyle(
                color = PRIMARY_BLACK,
                fontSize = 14.sp,
                fontFamily = montserratFamily
            ),
            singleLine = false,
            modifier = Modifier
                .fillMaxWidth()
        )

    }
}