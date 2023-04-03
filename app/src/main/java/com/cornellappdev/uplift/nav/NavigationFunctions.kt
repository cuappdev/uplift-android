package com.cornellappdev.uplift.nav

import androidx.navigation.NavHostController
import com.cornellappdev.uplift.models.Gym
import com.cornellappdev.uplift.models.UpliftClass
import com.cornellappdev.uplift.ui.viewmodels.ClassDetailViewModel
import com.cornellappdev.uplift.ui.viewmodels.GymDetailViewModel

fun NavHostController.navigateToClass(
    classDetailViewModel: ClassDetailViewModel,
    thisClass: UpliftClass
) {
    // Opens the new class.
    classDetailViewModel.openClass(thisClass)
    navigate("classDetail")
}

fun NavHostController.navigateToGym(
    gymDetailViewModel: GymDetailViewModel,
    gym: Gym
) {
    // Opens the new gym.
    gymDetailViewModel.openGym(gym)
    navigate("gymDetail")
}

fun NavHostController.popBackClass(
    classDetailViewModel: ClassDetailViewModel
) {
    classDetailViewModel.popBackStack()
    popBackStack()
}

fun NavHostController.popBackGym(
    gymDetailViewModel: GymDetailViewModel
) {
    gymDetailViewModel.popBackStack()
    popBackStack()
}