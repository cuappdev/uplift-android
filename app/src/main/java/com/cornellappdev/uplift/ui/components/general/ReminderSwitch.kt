package com.cornellappdev.uplift.ui.components.general

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cornellappdev.uplift.util.PRIMARY_YELLOW

@Composable
fun ReminderSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit = {}
) {
    Switch(
        checked = checked,
        onCheckedChange = {
            onCheckedChange(it)
        },
        colors = SwitchDefaults.colors(
            checkedTrackColor = PRIMARY_YELLOW,
            uncheckedThumbColor = Color.White,
            uncheckedTrackColor = Color(0xffE0E0E0),
            uncheckedBorderColor = Color(0xffE0E0E0),
        ),
        thumbContent = {
            Surface(
                shape = CircleShape,
                color = Color.White,
                modifier = Modifier
                    .size(40.dp)
                    .padding(0.dp),
                shadowElevation = 4.dp
            ) {}
        }
    )
}