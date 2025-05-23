package com.cornellappdev.uplift.ui.screens.classes

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.util.*

/**
 * Builds Filtering Screen for Class Screen with insturctors, classes to be added dynamically in the future.
 * Data from Filtering Screen will be sent to Classes Screen to filter out classes that are needed.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilteringScreen() {
    var isExpanded by remember { mutableStateOf(false) }
    var isExpandedInstructors by remember { mutableStateOf(false) }

    Column(
    ) {
        var newman by remember { mutableStateOf(false) }
        var noyes by remember { mutableStateOf(false) }
        var teagle by remember { mutableStateOf(false) }
        var appel by remember { mutableStateOf(false) }
        TopAppBar(
            backgroundColor = GRAY01
        ) {

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Reset",
                    modifier = Modifier
                        .clickable {}
                        .padding(start = 16.dp, top = 18.dp)
                        .weight(1.5F),
                    fontSize = 14.sp,
                    fontFamily = montserratFamily,
                    fontWeight = FontWeight(500),
                    color = PRIMARY_BLACK)
                Text(
                    text = "Refine Search",
                    modifier = Modifier
                        .padding(top = 18.dp)
                        .weight(2F),
                    fontSize = 14.sp,
                    fontFamily = montserratFamily,
                    fontWeight = FontWeight(700),
                    color = PRIMARY_BLACK,

                    )
                Text(text = "Done",
                    modifier = Modifier
                        .clickable { }
                        .padding(end = 16.dp, top = 18.dp),
                    fontSize = 14.sp,
                    fontFamily = montserratFamily,
                    fontWeight = FontWeight(500),
                    color = PRIMARY_BLACK)
            }
        }

        Text(
            text = "Fitness Centers",
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp),
            fontSize = 12.sp,
            fontFamily = montserratFamily,
            fontWeight = FontWeight(700),
            color = GRAY04
        )
        Row(
            modifier = Modifier
                .padding(top = 16.dp, bottom = 24.dp)
                .fillMaxWidth()
                .height(28.dp)
        ) {
            Text(
                text = "Noyes",
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp)
                    .clickable {
                        noyes = !noyes
                    },
                fontSize = 14.sp,
                fontFamily = montserratFamily,
                fontWeight = if (noyes) FontWeight.Bold else FontWeight(300),
                color = PRIMARY_BLACK
            )
            VerticalDivider(color = GRAY01, thickness = 1.dp)
            Text(
                text = "Helen Newman",
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp)
                    .clickable {
                        newman = !newman
                    },
                fontSize = 14.sp,
                fontFamily = montserratFamily,
                fontWeight = if (newman) FontWeight.Bold else FontWeight(300),
                color = PRIMARY_BLACK
            )
            VerticalDivider(color = GRAY01, thickness = 1.dp)
            Text(
                text = "Teagle",
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp)
                    .clickable {
                        teagle = !teagle
                    },
                fontSize = 14.sp,
                fontFamily = montserratFamily,
                fontWeight = if (teagle) FontWeight.Bold else FontWeight(300),
                color = PRIMARY_BLACK
            )
            VerticalDivider(color = GRAY01, thickness = 1.dp)
            Text(
                text = "Appel",
                modifier = Modifier
                    .padding(start = 12.dp)
                    .clickable {
                        appel = !appel
                    },
                fontSize = 14.sp,
                fontFamily = montserratFamily,
                fontWeight = if (appel) FontWeight.Bold else FontWeight(300),
                color = PRIMARY_BLACK
            )
        }
        TabRowDefaults.Divider(modifier = Modifier.fillMaxWidth(), color = GRAY01, thickness = 1.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Text(
                text = "Start Time",
                modifier = Modifier.padding(start = 16.dp),
                fontSize = 12.sp,
                fontFamily = montserratFamily,
                fontWeight = FontWeight(700),
                color = GRAY04
            )
            Spacer(modifier = Modifier.weight(1F))
            Text(
                text = "6:00AM-10:00PM",
                modifier = Modifier.padding(end = 16.dp),
                fontSize = 12.sp,
                fontFamily = montserratFamily,
                fontWeight = FontWeight(700),
                color = GRAY04
            )
        }
        var sliderPos by remember { mutableStateOf(0f..16F) }
        RangeSlider(
            value = sliderPos,
            onValueChange = { sliderPos = it },
            valueRange = 0f..16F,
            steps = 16,
            modifier = Modifier.padding(16.dp),
            colors = SliderDefaults.colors(
                thumbColor = Color.White, activeTrackColor = PRIMARY_YELLOW
            )
        )
        TabRowDefaults.Divider(modifier = Modifier.fillMaxWidth(), color = GRAY01, thickness = 1.dp)
        Column(modifier = Modifier.padding(top = 24.dp, bottom = 24.dp)) {
            Row {
                Text(
                    text = "Class Type",
                    modifier = Modifier.padding(start = 16.dp),
                    fontSize = 12.sp,
                    fontFamily = montserratFamily,
                    fontWeight = FontWeight(700),
                    color = GRAY04
                )
                Spacer(modifier = Modifier.weight(1F))
                Image(painter = painterResource(id = R.drawable.ic_caret_right),
                    contentDescription = "Right Arrow",
                    colorFilter = ColorFilter.tint(GRAY03),
                    modifier = Modifier
                        .padding(end = 25.dp)
                        .width(8.dp)
                        .height(12.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { isExpanded = !isExpanded })
            }
            if (isExpanded) {
                ExpandableViewClasses()
            }
        }
        Divider(modifier = Modifier.fillMaxWidth(), color = GRAY01, thickness = 1.dp)
        Column(modifier = Modifier.padding(top = 24.dp)) {
            Row {
                Text(
                    text = "Instructors",
                    modifier = Modifier.padding(start = 16.dp),
                    fontSize = 12.sp,
                    fontFamily = montserratFamily,
                    fontWeight = FontWeight(700),
                    color = GRAY04
                )
                Spacer(modifier = Modifier.weight(1F))
                Image(painter = painterResource(id = R.drawable.ic_caret_right),
                    contentDescription = "Right Arrow",
                    colorFilter = ColorFilter.tint(GRAY03),
                    modifier = Modifier
                        .padding(end = 25.dp)
                        .width(8.dp)
                        .height(12.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { isExpandedInstructors = !isExpandedInstructors })
            }
            if (isExpandedInstructors) {
                ExpandableViewInstructors()
            }
        }
    }
}

/**
 * Parameters: boolean ot show wheter its been expanded
 * Builds unexpanded view for classes filtering.
 */
@Composable
fun HeaderView(isExpanded: Boolean) {
    Row {
        Text(
            text = "Class Type",
            modifier = Modifier.padding(start = 16.dp),
            fontSize = 12.sp,
            fontFamily = montserratFamily,
            fontWeight = FontWeight(700),
            color = GRAY04
        )
        Spacer(modifier = Modifier.weight(1F))
        Image(painter = painterResource(id = R.drawable.ic_caret_right),
            contentDescription = "Right Arrow",
            modifier = Modifier
                .width(40.dp)
                .height(36.dp)
                .padding(end = 15.dp)
                .clickable {
                    //  isExpanded1 = !isExpanded1
                })
    }
}

/**
 *
 * Builds Expanded View for classes with list of class types to filter
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExpandableViewClasses() {
    var barrchecked by remember { mutableStateOf(false) }
    var hiitchecked by remember { mutableStateOf(false) }
    var spinchecked by remember { mutableStateOf(false) }
    Column {
        Row(modifier = Modifier.padding(top = 15.dp)) {
            Text(
                text = "Spinning",
                fontSize = 14.sp,
                color = PRIMARY_BLACK,
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(Modifier.weight(1F))
            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                Checkbox(
                    checked = spinchecked,
                    onCheckedChange = { spinchecked = !spinchecked },
                    modifier = Modifier.padding(end = 23.dp),
                    colors = CheckboxDefaults.colors(
                        checkedColor = PRIMARY_YELLOW, uncheckedColor = Color.Black
                    )
                )
            }
        }
        Row(modifier = Modifier.padding(top = 8.dp)) {
            Text(
                text = "Barre",
                fontSize = 14.sp,
                color = PRIMARY_BLACK,
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(Modifier.weight(1F))
            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                Checkbox(
                    checked = barrchecked,
                    onCheckedChange = { barrchecked = !barrchecked },
                    modifier = Modifier.padding(end = 23.dp),
                    colors = CheckboxDefaults.colors(
                        checkedColor = PRIMARY_YELLOW, uncheckedColor = Color.Black
                    )
                )
            }
        }
        Row(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)) {
            Text(
                text = "HIIT",
                fontSize = 14.sp,
                color = PRIMARY_BLACK,
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(Modifier.weight(1F))
            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                Checkbox(
                    checked = hiitchecked,
                    onCheckedChange = { hiitchecked = !hiitchecked },
                    modifier = Modifier.padding(end = 23.dp),
                    colors = CheckboxDefaults.colors(
                        checkedColor = PRIMARY_YELLOW, uncheckedColor = Color.Black
                    )
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

/**
 * Builds expandend View for instructors filtering class with list of instructors
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExpandableViewInstructors() {
    var onechecked by remember { mutableStateOf(false) }
    var twochecked by remember { mutableStateOf(false) }
    var threechecked by remember { mutableStateOf(false) }
    Column {
        Row(modifier = Modifier.padding(top = 15.dp)) {
            Text(
                text = "Emily",
                fontSize = 14.sp,
                color = PRIMARY_BLACK,
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(Modifier.weight(1F))
            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                Checkbox(
                    checked = onechecked,
                    onCheckedChange = { onechecked = !onechecked },
                    modifier = Modifier.padding(end = 23.dp),
                    colors = CheckboxDefaults.colors(
                        checkedColor = PRIMARY_YELLOW, uncheckedColor = Color.Black
                    )
                )
            }
        }
        Row(modifier = Modifier.padding(top = 8.dp)) {
            Text(
                text = "Lauren",
                fontSize = 14.sp,
                color = PRIMARY_BLACK,
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(Modifier.weight(1F))
            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                Checkbox(
                    checked = twochecked,
                    onCheckedChange = { twochecked = !twochecked },
                    modifier = Modifier.padding(end = 23.dp),
                    colors = CheckboxDefaults.colors(
                        checkedColor = PRIMARY_YELLOW, uncheckedColor = Color.Black
                    )
                )
            }
        }
        Row(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)) {
            Text(
                text = "HIIT",
                fontSize = 14.sp,
                color = PRIMARY_BLACK,
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(Modifier.weight(1F))
            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                Checkbox(
                    checked = threechecked,
                    onCheckedChange = { threechecked = !threechecked },
                    modifier = Modifier.padding(end = 23.dp),
                    colors = CheckboxDefaults.colors(
                        checkedColor = PRIMARY_YELLOW, uncheckedColor = Color.Black
                    )
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

/**
 * Vertical Line to seperate text entries in a row.
 */
@Composable
fun VerticalDivider(
    modifier: Modifier = Modifier, color: Color, thickness: Dp = 1.dp
) {
    Box(
        modifier
            .fillMaxHeight()
            .width(thickness)
            .background(color = color)
    )
}
