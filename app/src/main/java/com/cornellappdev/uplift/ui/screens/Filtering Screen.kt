package com.cornellappdev.uplift.ui.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun filteringScreen(){
    var isExpanded by remember{ mutableStateOf(false) }
    var isExpandedInstructors by remember{ mutableStateOf(false) }


    Column() {
        var newman by remember { mutableStateOf(false) }
        var noyes by remember { mutableStateOf(false) }
        var teagle by remember { mutableStateOf(false) }
        var appel by remember { mutableStateOf(false) }
        TopAppBar(
            backgroundColor = GRAY01
        )
        {

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Reset",
                    modifier = Modifier
                        .clickable { }
                        .padding(start = 16.dp),
                    fontSize = 14.sp,
                    fontFamily = montserratFamily,
                    fontWeight = FontWeight(500),
                    color = PRIMARY_BLACK
                )
                Text(
                    text = "Refine Search",
                    modifier = Modifier.padding(start=80.dp, end=80.dp),
                    fontSize = 14.sp,
                    fontFamily = montserratFamily,
                    fontWeight = FontWeight(700),
                    color = PRIMARY_BLACK,

                    )
                Text(
                    text = "Done",
                    modifier = Modifier
                        .clickable { }
                        .padding(end = 16.dp),
                    fontSize = 14.sp,
                    fontFamily = montserratFamily,
                    fontWeight = FontWeight(500),
                    color = PRIMARY_BLACK
                )
            }
        }


        Text(
            text = "Fitness Centers",
            modifier = Modifier
                .padding(start = 16.dp, end=16.dp, top= 24.dp),
            fontSize = 12.sp,
            fontFamily = montserratFamily,
            fontWeight = FontWeight(700),
            color = GRAY04
        )
        Row(modifier= Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 24.dp)
            .height(28.dp))
        {
            Text(
                text = "Noyes",
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .clickable {
                        if (noyes) {

                        }
                    },
                fontSize = 14.sp,
                fontFamily = montserratFamily,
                fontWeight = FontWeight(300),
                color = PRIMARY_BLACK
            )
            VerticalDivider(color = GRAY01, thickness = 1.dp)
            Text(
                text = "Helen Newman",
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .clickable {
                    },
                fontSize = 14.sp,
                fontFamily = montserratFamily,
                fontWeight = FontWeight(300),
                color = PRIMARY_BLACK
            )
            VerticalDivider(color = GRAY01, thickness = 1.dp)
            Text(
                text = "Teagle",
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .clickable {
                    },
                fontSize = 14.sp,
                fontFamily = montserratFamily,
                fontWeight = FontWeight(300),
                color = PRIMARY_BLACK
            )
            VerticalDivider(color = GRAY01, thickness = 1.dp)
            Text(
                text = "Appel",
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .clickable {
                    },
                fontSize = 14.sp,
                fontFamily = montserratFamily,
                fontWeight = FontWeight(300),
                color = PRIMARY_BLACK
            )
        }
        TabRowDefaults.Divider(modifier= Modifier.fillMaxWidth(), color= GRAY01, thickness=1.dp)
        Row(modifier= Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)){
            Text(
                text = "Start Time",
                modifier = Modifier
                    .padding(start = 16.dp )
                ,
                fontSize = 12.sp,
                fontFamily = montserratFamily,
                fontWeight = FontWeight(700),
                color = GRAY04
            )
            Spacer(modifier= Modifier.weight(1F))
            Text(
                text = "6:00AM-10:00PM",
                modifier = Modifier
                    .padding(end = 16.dp )
                ,
                fontSize = 12.sp,
                fontFamily = montserratFamily,
                fontWeight = FontWeight(700),
                color = GRAY04
            )
        }
        var sliderPos by remember { mutableStateOf(0f..16F) }
        RangeSlider(values = sliderPos, onValueChange = {sliderPos=it},valueRange= 0f..16F, steps=16, modifier= Modifier.padding(16.dp))
        TabRowDefaults.Divider(modifier= Modifier.fillMaxWidth(), color= GRAY01, thickness=1.dp)
        Column(modifier=Modifier.padding(top=24.dp, bottom=24.dp)) {
            Row {
                Text(
                    text = "Class Type",
                    modifier = Modifier
                        .padding(start = 16.dp),
                    fontSize = 12.sp,
                    fontFamily = montserratFamily,
                    fontWeight = FontWeight(700),
                    color = GRAY04
                )
                Spacer(modifier=Modifier.weight(1F))
                Image(
                    painter = painterResource(id = R.drawable.ic_caret_right),
                    contentDescription = "Right Arrow",
                    colorFilter = ColorFilter.tint(GRAY03),
                    modifier= Modifier
                        .padding(end = 25.dp)
                        .width(8.dp)
                        .height(12.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        )
                        { isExpanded = !isExpanded }
                )
            }
            if(isExpanded) {
              expandbleViewClasses()
            }
        }
        Divider(modifier= Modifier.fillMaxWidth(), color= GRAY01, thickness=1.dp)
        Column(modifier=Modifier.padding(top=24.dp)){
            Row {
                Text(
                    text = "Instructors",
                    modifier = Modifier
                        .padding(start = 16.dp),
                    fontSize = 12.sp,
                    fontFamily = montserratFamily,
                    fontWeight = FontWeight(700),
                    color = GRAY04
                )
                Spacer(modifier=Modifier.weight(1F))
                Image(
                    painter = painterResource(id = R.drawable.ic_caret_right),
                    contentDescription = "Right Arrow",
                    colorFilter = ColorFilter.tint(GRAY03),
                    modifier= Modifier
                        .padding(end = 25.dp)
                        .width(8.dp)
                        .height(12.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        )
                        { isExpandedInstructors = !isExpandedInstructors }
                )
            }
            if(isExpandedInstructors){
                expandbleViewInstructors()
            }
        }
    }
}
@Composable
fun headerView(isExpanded1: MutableState<Boolean>){
    Row {
        Text(
            text = "Class Type",
            modifier = Modifier
                .padding(start = 16.dp),
            fontSize = 12.sp,
            fontFamily = montserratFamily,
            fontWeight = FontWeight(700),
            color = GRAY04
        )
        Spacer(modifier=Modifier.weight(1F))
        Image(
            painter = painterResource(id = R.drawable.ic_caret_right),
            contentDescription = "Right Arrow",
            modifier= Modifier
                .width(40.dp)
                .height(36.dp)
                .padding(end = 15.dp)
                .clickable {
                    //  isExpanded1 = !isExpanded1
                }
        )
    }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun expandbleViewClasses() {
    var barrchecked by remember { mutableStateOf(false) }
    var hiitchecked by remember { mutableStateOf(false) }
    var spinchecked by remember { mutableStateOf(false) }
    Column {
        Row(modifier = Modifier.padding(top = 15.dp)) {
            Text(
                text = "Spinning",
                fontSize = 14.sp,
                color = PRIMARY_BLACK,
                modifier = Modifier
                    .padding(start = 16.dp)
            )
            Spacer(Modifier.weight(1F))
            CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                Checkbox(
                    checked = spinchecked,
                    onCheckedChange = { spinchecked = !spinchecked },
                    modifier = Modifier.padding(end = 23.dp),
                    colors = CheckboxDefaults.colors(PRIMARY_YELLOW_BACKGROUND)
                )
            }
        }
            Row(modifier=Modifier.padding(top=8.dp)) {
                Text(
                    text = "Barre",
                    fontSize = 14.sp,
                    color = PRIMARY_BLACK,
                    modifier = Modifier
                        .padding(start = 16.dp)
                )
                Spacer(Modifier.weight(1F))
                CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                    Checkbox(
                        checked = barrchecked,
                        onCheckedChange = { barrchecked = !barrchecked },
                        modifier = Modifier.padding(end = 23.dp),
                        colors = CheckboxDefaults.colors(PRIMARY_YELLOW_BACKGROUND)
                    )
                }
            }
                Row(modifier = Modifier.padding(top=8.dp,bottom = 8.dp)) {
                    Text(
                        text = "HIIT",
                        fontSize = 14.sp,
                        color = PRIMARY_BLACK,
                        modifier = Modifier
                            .padding(start = 16.dp)
                    )
                    Spacer(Modifier.weight(1F))
                    CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                        Checkbox(
                            checked = hiitchecked,
                            onCheckedChange = { hiitchecked = !hiitchecked },
                            modifier = Modifier.padding(end = 23.dp),
                            colors = CheckboxDefaults.colors(PRIMARY_YELLOW_BACKGROUND)
                        )
                    }
                }
                Text(
                    text = "Show All Class Types",
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .clickable {},
                    fontSize = 12.sp,
                    fontFamily = montserratFamily,
                    fontWeight = FontWeight(500),
                    color = GRAY02
                )
            }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun expandbleViewInstructors() {
    var onechecked by remember { mutableStateOf(false) }
    var twochecked by remember { mutableStateOf(false) }
    var threechecked by remember { mutableStateOf(false) }
    Column {
        Row(modifier = Modifier.padding(top = 15.dp)) {
            Text(
                text = "Emily",
                fontSize = 14.sp,
                color = PRIMARY_BLACK,
                modifier = Modifier
                    .padding(start = 16.dp)
            )
            Spacer(Modifier.weight(1F))
            CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                Checkbox(
                    checked = onechecked,
                    onCheckedChange = { onechecked = !onechecked },
                    modifier = Modifier.padding(end = 23.dp),
                    colors = CheckboxDefaults.colors(PRIMARY_YELLOW_BACKGROUND)
                )
            }
        }
        Row(modifier=Modifier.padding(top=8.dp)) {
            Text(
                text = "Lauren",
                fontSize = 14.sp,
                color = PRIMARY_BLACK,
                modifier = Modifier
                    .padding(start = 16.dp)
            )
            Spacer(Modifier.weight(1F))
            CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                Checkbox(
                    checked = twochecked,
                    onCheckedChange = { twochecked = !twochecked },
                    modifier = Modifier.padding(end = 23.dp),
                    colors = CheckboxDefaults.colors(PRIMARY_YELLOW_BACKGROUND)
                )
            }
        }
        Row(modifier = Modifier.padding(top=8.dp,bottom = 8.dp)) {
            Text(
                text = "HIIT",
                fontSize = 14.sp,
                color = PRIMARY_BLACK,
                modifier = Modifier
                    .padding(start = 16.dp)
            )
            Spacer(Modifier.weight(1F))
            CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                Checkbox(
                    checked = threechecked,
                    onCheckedChange = { threechecked= !threechecked },
                    modifier = Modifier.padding(end = 23.dp),
                    colors = CheckboxDefaults.colors(PRIMARY_YELLOW_BACKGROUND)
                )
            }
        }
        Text(
            text = "Show All Instructors",
            modifier = Modifier
                .padding(start = 16.dp)
                .clickable {},
            fontSize = 12.sp,
            fontFamily = montserratFamily,
            fontWeight = FontWeight(500),
            color = GRAY02
        )
    }
}
@Composable
fun VerticalDivider(
    modifier: Modifier = Modifier,
    color: Color,
    thickness: Dp = 1.dp
) {
    Box(
        modifier
            .fillMaxHeight()
            .width(thickness)
            .background(color = color)
    )
}