package com.cornellappdev.uplift.ui.screens.onboarding

import android.net.Uri
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.components.onboarding.PhotoPicker
import com.cornellappdev.uplift.ui.viewmodels.onboarding.ProfileCreationViewModel
import com.cornellappdev.uplift.util.GRAY01
import com.cornellappdev.uplift.util.GRAY02
import com.cornellappdev.uplift.util.GRAY03
import com.cornellappdev.uplift.util.PRIMARY_YELLOW
import com.cornellappdev.uplift.util.montserratFamily
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction0

/**
 * The profile creation page during the Uplift onboarding process.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileCreationScreen(
    profileCreationViewModel: ProfileCreationViewModel
) {
    val checkboxColors: CheckboxColors =
        CheckboxDefaults.colors(
            checkedColor = PRIMARY_YELLOW,
            checkmarkColor = Color.Black,
            uncheckedColor = GRAY03
        )

    val profileCreationUiState = profileCreationViewModel.collectUiStateValue()
    val name = profileCreationUiState.name
    val coroutineScope = rememberCoroutineScope()

    val eulaAgreeCheckedState = remember { mutableStateOf(false) }
    val gymDataAccessCheckedState = remember { mutableStateOf(false) }
    val locationAccessCheckedState = remember { mutableStateOf(false) }
    val allChecked =
        eulaAgreeCheckedState.value && gymDataAccessCheckedState.value && locationAccessCheckedState.value

    val animatedOpacity: Float by animateFloatAsState(
        if (allChecked) 1f else 0f,
        label = "Uplift Text Opacity"
    )
    val opacityModifier: Modifier = Modifier.alpha(animatedOpacity)

    ProfileCreationScreenContent(
        profileCreationViewModel::onPhotoSelected,
        profileCreationViewModel::createUser,
        name,
        eulaAgreeCheckedState,
        checkboxColors,
        gymDataAccessCheckedState,
        locationAccessCheckedState,
        opacityModifier,
        coroutineScope,
        allChecked
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ProfileCreationScreenContent(
    onPhotoSelected: (Uri) -> Unit,
    createUser: () -> Unit,
    name: String,
    eulaAgreeCheckedState: MutableState<Boolean>,
    checkboxColors: CheckboxColors,
    gymDataAccessCheckedState: MutableState<Boolean>,
    locationAccessCheckedState: MutableState<Boolean>,
    opacityModifier: Modifier,
    coroutineScope: CoroutineScope,
    allChecked: Boolean
) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Complete your profile.",
                        modifier = Modifier.padding(top = 20.dp),
                        fontSize = 24.sp,
                        fontFamily = montserratFamily,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Left
                    )
                },
                modifier = Modifier
                    .height(120.dp)
                    .shadow(elevation = 20.dp, ambientColor = GRAY01),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                )

            )
        }) { innerPadding ->

        Column(
            modifier = Modifier
                .padding((innerPadding))
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.05f))

            PhotoPicker { onPhotoSelected(it) }

            Text(
                text = name,
                modifier = Modifier.padding(vertical = 24.dp),
                fontSize = 24.sp,
                fontFamily = montserratFamily,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(0.03f))

            CheckBoxesSection(
                eulaAgreeCheckedState,
                checkboxColors,
                gymDataAccessCheckedState,
                locationAccessCheckedState
            )

            Spacer(modifier = Modifier.weight(0.17f))

            ReadyToUplift(opacityModifier)
            NextButton(coroutineScope, createUser, allChecked)

            Spacer(modifier = Modifier.weight(0.09f))

        }
    }
}

@Composable
private fun CheckBoxesSection(
    checkedState: MutableState<Boolean>,
    checkboxColors: CheckboxColors,
    checkedState2: MutableState<Boolean>,
    checkedState3: MutableState<Boolean>
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        InfoCheckboxRow(
            checkedState = checkedState,
            checkboxColors = checkboxColors,
            message = "I agree with EULA terms and agreements"
        )

        InfoCheckboxRow(
            checkedState = checkedState2,
            checkboxColors = checkboxColors,
            message = "I allow Uplift to access data on my gym usage"
        )

        InfoCheckboxRow(
            checkedState = checkedState3,
            checkboxColors = checkboxColors,
            message = "I allow Uplift to access my location"
        )

    }
}

@Composable
private fun NextButton(
    coroutineScope: CoroutineScope,
    onClick: () -> Unit,
    allChecked: Boolean
) {
    Button(
        {
            coroutineScope.launch {
                onClick()
            }
        },
        enabled = allChecked,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = PRIMARY_YELLOW,
            disabledBackgroundColor = GRAY02,
            contentColor = Color.Black
        ),
        shape = RoundedCornerShape(38.dp),
        modifier = Modifier.size(height = 44.dp, width = 144.dp)
    ) {

        Text(
            text = if (allChecked) "Get started" else "Next",
            fontSize = 16.sp,
            fontFamily = montserratFamily,
            fontWeight = FontWeight.Bold
        )

    }
}

@Composable
private fun ReadyToUplift(
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(bottom = 25.dp)
    ) {
        Text(
            text = "Are you ready to",
            fontSize = 16.sp,
            fontFamily = montserratFamily,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.width(6.dp))

        Image(
            painter = painterResource(R.drawable.ic_main_logo),
            contentDescription = "",
            modifier = Modifier.size(width = 26.dp, height = 23.dp)
        )

        Image(
            painter = painterResource(R.drawable.lift_logo),
            contentDescription = "",
        )
        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = "?",
            fontSize = 16.sp,
            fontFamily = montserratFamily,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun InfoCheckboxRow(
    checkedState: MutableState<Boolean>,
    checkboxColors: CheckboxColors,
    message: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = checkedState.value,
            onCheckedChange = { checkedState.value = it },
            colors = checkboxColors
        )
        Text(
            text = message,
            fontSize = 12.sp,
            fontFamily = montserratFamily,
            fontWeight = FontWeight.Normal
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileCreationScreenPreview() {
    val eulaAgreeCheckedState = remember { mutableStateOf(false) }
    val gymDataAccessCheckedState = remember { mutableStateOf(false) }
    val locationAccessCheckedState = remember { mutableStateOf(false) }

    ProfileCreationScreenContent(
        onPhotoSelected = {},
        createUser = {},
        name = "John Doe",
        eulaAgreeCheckedState = eulaAgreeCheckedState,
        checkboxColors = CheckboxDefaults.colors(),
        gymDataAccessCheckedState = gymDataAccessCheckedState,
        locationAccessCheckedState = locationAccessCheckedState,
        opacityModifier = Modifier.alpha(1f),
        coroutineScope = rememberCoroutineScope(),
        allChecked = false
    )
}