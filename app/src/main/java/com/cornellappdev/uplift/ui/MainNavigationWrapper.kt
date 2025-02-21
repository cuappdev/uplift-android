package com.cornellappdev.uplift.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cornellappdev.uplift.ui.nav.BottomNavScreens
import com.cornellappdev.uplift.ui.nav.popBackClass
import com.cornellappdev.uplift.ui.nav.popBackGym
import com.cornellappdev.uplift.data.models.ApiResponse
import com.cornellappdev.uplift.ui.screens.classes.ClassDetailScreen
import com.cornellappdev.uplift.ui.screens.classes.ClassScreen
import com.cornellappdev.uplift.ui.screens.gyms.GymDetailScreen
import com.cornellappdev.uplift.ui.screens.gyms.HomeScreen
import com.cornellappdev.uplift.ui.screens.onboarding.ProfileCreationScreen
import com.cornellappdev.uplift.ui.screens.onboarding.SignInPromptScreen
import com.cornellappdev.uplift.ui.screens.report.ReportIssueScreen
import com.cornellappdev.uplift.ui.screens.report.ReportSubmittedScreen
import com.cornellappdev.uplift.ui.viewmodels.classes.ClassDetailViewModel
import com.cornellappdev.uplift.ui.viewmodels.classes.ClassesViewModel
import com.cornellappdev.uplift.ui.viewmodels.gyms.GymDetailViewModel
import com.cornellappdev.uplift.ui.viewmodels.gyms.HomeViewModel
import com.cornellappdev.uplift.ui.viewmodels.auth.LoginViewModel
import com.cornellappdev.uplift.ui.viewmodels.report.ReportViewModel
import com.cornellappdev.uplift.ui.viewmodels.nav.RootNavigationViewModel
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.PRIMARY_YELLOW
import com.cornellappdev.uplift.util.montserratFamily
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.defaultShimmerTheme
import com.valentinilk.shimmer.rememberShimmer
import kotlinx.serialization.Serializable

/**
 * The main navigation controller for the app.
 */
@Composable
fun MainNavigationWrapper(
    gymDetailViewModel: GymDetailViewModel = hiltViewModel(),
    classDetailViewModel: ClassDetailViewModel = hiltViewModel(),
    classesViewModel: ClassesViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel(),
    reportViewModel: ReportViewModel = hiltViewModel(),
    rootNavigationViewModel: RootNavigationViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel = hiltViewModel()
) {

    val uiState = rootNavigationViewModel.collectUiStateValue()
    val navController = rememberNavController()
    val systemUiController: SystemUiController = rememberSystemUiController()
    val gymsState = homeViewModel.gymFlow.collectAsState().value

    val yourShimmerTheme = defaultShimmerTheme.copy(
        shaderColors = listOf(
            Color.Unspecified.copy(alpha = 1f),
            Color.Unspecified.copy(alpha = .25f),
            Color.Unspecified.copy(alpha = 1f)
        )
    )
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.Window, theme = yourShimmerTheme)

    val items = listOf(
        BottomNavScreens.Home,
        BottomNavScreens.Classes,
        // TODO: Add new items when activities and profile are implemented.
    )

    systemUiController.setStatusBarColor(PRIMARY_YELLOW)

    LaunchedEffect(uiState.navEvent) {
        uiState.navEvent?.consumeSuspend {
            // Navigate.
            navController.navigate(it)
        }
    }
    LaunchedEffect(uiState.popBackStack) {
        uiState.popBackStack?.consumeSuspend {
            navController.popBackStack()
        }
    }

    //TODO() : Once Google Sign In is fully implemented, make sure BottomNavBar not visible until user signs in
    Scaffold(bottomBar = {
        if (gymsState is ApiResponse.Success) BottomNavigation(
            backgroundColor = PRIMARY_YELLOW, contentColor = PRIMARY_BLACK, elevation = 1.dp
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            items.forEach { screen ->
                val selected =
                    currentDestination?.hierarchy?.any {
                        it.route == screen.route::class.qualifiedName
                    } == true
                BottomNavigationItem(icon = {
                    Icon(
                        painter = painterResource(
                            id = if (selected) screen.painterIds.second else screen.painterIds.first
                        ),
                        contentDescription = null
                    )
                }, label = {
                    Text(
                        text = screen.titleText,
                        fontFamily = montserratFamily,
                        fontSize = 14.sp,
                        fontWeight = if (selected) FontWeight(700)
                        else FontWeight(500),
                        lineHeight = 17.07.sp,
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Visible,
                        softWrap = false
                    )
                }, selected = selected, onClick = {
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                })
            }
        }
    }) {
        // TODO 1: Change back to home if releasing features before google sign in is finished.
        // TODO 2: Change to Onboarding after google sign in is finished
        NavHost(
            navController = navController,
            startDestination = UpliftRootRoute.Onboarding,
            modifier = Modifier.padding(it)
        ) {
            composable<UpliftRootRoute.Home> {
                HomeScreen(
                    homeViewModel = homeViewModel,
                    navController = navController,
                    classDetailViewModel = classDetailViewModel,
                    gymDetailViewModel = gymDetailViewModel,
                    loadingShimmer = shimmer
                )
            }
            composable<UpliftRootRoute.GymDetail> {
                GymDetailScreen(
                    gymDetailViewModel = gymDetailViewModel,
                ) {
                    navController.popBackGym(gymDetailViewModel)
                }
            }
            // TODO: I split these across multiple screens to make it so the user sticks to
            //  the side of the app they were originally on.
            //  However, I think this might cause a bug if you have classes open on BOTH
            //  sides of the app then pop back. I think both will end up popping back.
            //  I can't test RN cuz backend is down, so test this and see. If it's broken,
            //  change it to just use one "classDetail" route on the class half.
            composable<UpliftRootRoute.ClassDetail> {
                ClassDetailScreen(
                    classDetailViewModel = classDetailViewModel, navController = navController
                ) {
                    navController.popBackClass(classDetailViewModel)
                }
            }
            composable<UpliftRootRoute.Classes> {
                ClassScreen(
                    classDetailViewModel = classDetailViewModel,
                    navController = navController,
                    classesViewModel = classesViewModel
                )
            }
            composable<UpliftRootRoute.ReportIssue> {
                ReportIssueScreen(
                    onSubmit = reportViewModel::createReport,
                    onBack = { navController.popBackStack() }
                )
            }
            composable<UpliftRootRoute.Onboarding> {
                SignInPromptScreen(loginViewModel, loginViewModel::navigateToHome)
            }
            composable<UpliftRootRoute.ProfileCreation> {
                ProfileCreationScreen(navController = navController)
            }
            composable<UpliftRootRoute.ReportSuccess> {
                ReportSubmittedScreen(
                    onSubmitAnother = reportViewModel::navigateToReport,
                    onReturnHome = reportViewModel::navigateToHome
                )
            }
            composable<UpliftRootRoute.Sports> {}
            composable<UpliftRootRoute.Favorites> {}
        }
    }
}

@Serializable
sealed class UpliftRootRoute {
    @Serializable
    data object Home : UpliftRootRoute()

    @Serializable
    data object GymDetail : UpliftRootRoute()

    @Serializable
    data object Onboarding : UpliftRootRoute()

    @Serializable
    data object ProfileCreation : UpliftRootRoute()

    @Serializable
    data object Classes : UpliftRootRoute()

    @Serializable
    data object ClassDetail : UpliftRootRoute()

    @Serializable
    data object Sports : UpliftRootRoute()

    @Serializable
    data object Favorites : UpliftRootRoute()

    @Serializable
    data object Profile : UpliftRootRoute()

    @Serializable
    data object ReportIssue : UpliftRootRoute()

    @Serializable
    data object ReportSuccess : UpliftRootRoute()
}
