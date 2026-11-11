package com.cornellappdev.uplift.ui.nav

import androidx.navigation.NavHostController
import com.cornellappdev.uplift.data.models.UpliftClass
import com.cornellappdev.uplift.data.models.gymdetail.UpliftGym
import com.cornellappdev.uplift.ui.UpliftRootRoute

fun NavHostController.navigateToClass(
    openClass: (UpliftClass) -> Unit,
    thisClass: UpliftClass
) {
    // Opens the new class.
    openClass(thisClass)
    navigate(UpliftRootRoute.ClassDetail)
}

fun NavHostController.navigateToGym(
    openGym: (UpliftGym) -> Unit,
    gym: UpliftGym
) {
    // Opens the new gym.
    openGym(gym)
    navigate(UpliftRootRoute.GymDetail)
}

fun NavHostController.popBackClass(
    classDetailPopBackStack: () -> Unit,
) {
    classDetailPopBackStack()
    popBackStack()
}

fun NavHostController.popBackGym(
    gymDetailsPopBackStack: () -> Unit,
) {
    gymDetailsPopBackStack()
    popBackStack()
}
