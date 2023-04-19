package com.cornellappdev.uplift.ui.components.classdetail

import android.content.Intent
import android.provider.CalendarContract
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.models.UpliftClass
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.montserratFamily

@Composable
fun ClassDateAndTime(upliftClass: UpliftClass?) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(24.dp))
        Text(
            text = upliftClass?.dateAsString() ?: "",
            fontWeight = FontWeight(300),
            fontSize = 16.sp,
            lineHeight = 19.5.sp,
            textAlign = TextAlign.Center,
            color = PRIMARY_BLACK,
            fontFamily = montserratFamily
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = upliftClass?.time.toString(),
            fontWeight = FontWeight(500),
            fontSize = 16.sp,
            lineHeight = 19.5.sp,
            textAlign = TextAlign.Center,
            color = PRIMARY_BLACK,
            fontFamily = montserratFamily
        )
        Spacer(Modifier.height(24.dp))
        Column(
            modifier = Modifier
                .clickable {
                    upliftClass?.let {
                        val intent = Intent(Intent.ACTION_EDIT)
                        intent.type = "vnd.android.cursor.item/event"
                        intent.putExtra("beginTime", it.time.start.timeInMillis(it.date))
                        intent.putExtra("allDay", false)
                        intent.putExtra("endTime", it.time.end.timeInMillis(it.date))
                        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, it.location)
                        intent.putExtra("title", it.name)
                        ContextCompat.startActivity(context, intent, null)
                    }
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_calendar),
                contentDescription = "Add to calendar",
                tint = PRIMARY_BLACK
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "ADD TO CALENDAR",
                fontWeight = FontWeight(700),
                fontSize = 12.sp,
                lineHeight = 14.63.sp,
                textAlign = TextAlign.Center,
                color = PRIMARY_BLACK,
                fontFamily = montserratFamily
            )
        }
        Spacer(Modifier.height(24.dp))
    }
}