package com.cornellappdev.uplift.ui.components.Home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.R
class HomeCard {

    @Composable
    fun homeCard(  name: Int) {
        Card(
            modifier= Modifier.fillMaxWidth().fillMaxHeight().aspectRatio(2F/1F)

        ) {
            Column() {
                Column(modifier = Modifier.weight(3F)) {
                    Image(
                        modifier = Modifier.fillMaxWidth(),
                        painter = painterResource(id = R.drawable.ic_btn_speak_now),
                        contentDescription = "test",
                        contentScale= ContentScale.FillWidth
                    )

                }
                Column(modifier = Modifier.weight(2F)) {
                    Row(
                        modifier = Modifier.padding(5.dp)
                    ) {
                        Text(
                            text = "Helen Newman",
                            fontSize = 20.sp,
                        )
                        Spacer(modifier = Modifier.weight(1F))
                        Image(
                            painter = painterResource(id = R.drawable.ic_menu_add),
                            contentDescription = "test",
                        )
                        Image(
                            painter = painterResource(id = R.drawable.ic_menu_search),
                            contentDescription = "test",
                        )

                    }
                    Row(modifier= Modifier.padding(start=5.dp, end=5.dp)) {
                        Text(
                            text = "Open",
                            fontSize = 12.sp,
                            color = Color.Green
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Closes at 6 pm",
                            fontSize = 12.sp,
                        )

                    }
                    Row(modifier= Modifier.padding(start=5.dp,
                        end= 5.dp)) {
                        Text(
                            text = "Cramped",
                            fontSize = 12.sp,
                            color = Color.Red
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = " 120/140",
                            fontSize = 12.sp,
                        )
                        Spacer(modifier= Modifier.weight(1F))
                        Text("1.2mi")
                    }
                }
            }
        }
    }

}