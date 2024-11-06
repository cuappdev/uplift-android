package com.cornellappdev.uplift.nav

import com.cornellappdev.uplift.ui.UpliftRootRoute
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RootNavigationRepository @Inject constructor() :
    BaseNavigationRepository<UpliftRootRoute>()