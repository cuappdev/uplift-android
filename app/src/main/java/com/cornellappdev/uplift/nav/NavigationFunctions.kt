package com.cornellappdev.uplift.nav

import androidx.navigation.NavHostController
import com.cornellappdev.uplift.models.UpliftClass
import com.cornellappdev.uplift.ui.viewmodels.ClassDetailViewModel

fun NavHostController.navigateClasses(
    classDetailViewModel: ClassDetailViewModel,
    thisClass: UpliftClass
) {
    // Adds the currently displayed class onto the stack.
    classDetailViewModel.classFlow.value?.let {
        classDetailViewModel.selectClass(it)
    }
    // Adds the new-to-display class onto the stack (will get popped off immediately).
    classDetailViewModel.selectClass(thisClass)
    navigate("classDetail")
}