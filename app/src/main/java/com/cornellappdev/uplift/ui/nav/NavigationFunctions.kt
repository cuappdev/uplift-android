package com.cornellappdev.uplift.ui.nav

import androidx.navigation.NavHostController
import com.cornellappdev.uplift.data.models.UpliftClass
import com.cornellappdev.uplift.data.models.UpliftGym
import com.cornellappdev.uplift.ui.UpliftRootRoute
import com.cornellappdev.uplift.ui.viewmodels.ClassDetailViewModel
import com.cornellappdev.uplift.ui.viewmodels.GymDetailViewModel

fun NavHostController.navigateToClass(
    classDetailViewModel: ClassDetailViewModel,
    thisClass: UpliftClass
) {
    // Opens the new class.
    classDetailViewModel.openClass(thisClass)
    navigate(UpliftRootRoute.ClassDetail)
//    if (currentDestination?.route.toString() == "classesMain") {
//        navigate("classDetailClasses")
//    } else {
//        navigate("classDetailHome")
//    }
}

fun NavHostController.navigateToGym(
    gymDetailViewModel: GymDetailViewModel,
    gym: UpliftGym
) {
    // Opens the new gym.
    gymDetailViewModel.openGym(gym)
    navigate(UpliftRootRoute.GymDetail)
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
