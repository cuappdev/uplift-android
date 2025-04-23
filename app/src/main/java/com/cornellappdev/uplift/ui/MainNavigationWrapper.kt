package com.cornellappdev.uplift.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.cornellappdev.uplift.ui.screens.classes.ClassDetailScreen
import com.cornellappdev.uplift.ui.screens.classes.ClassScreen
import com.cornellappdev.uplift.ui.screens.gyms.GymDetailScreen
import com.cornellappdev.uplift.ui.screens.gyms.HomeScreen
import com.cornellappdev.uplift.ui.screens.onboarding.ProfileCreationScreen
import com.cornellappdev.uplift.ui.screens.onboarding.SignInPromptScreen
import com.cornellappdev.uplift.ui.screens.profile.ProfileScreen
import com.cornellappdev.uplift.ui.screens.profile.SettingsScreen
import com.cornellappdev.uplift.ui.screens.reminders.MainReminderScreen
import com.cornellappdev.uplift.ui.screens.report.ReportIssueScreen
import com.cornellappdev.uplift.ui.screens.report.ReportSubmittedScreen
import com.cornellappdev.uplift.ui.viewmodels.classes.ClassDetailViewModel
import com.cornellappdev.uplift.ui.viewmodels.gyms.GymDetailViewModel
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
    // Note: For future view models, please add them to the screen they are used in instead of here.
    // TODO: Remove HomeScreen and ClassScreen dependencies on gymDetailViewModel and classDetailViewModel and then remove them from here.
    gymDetailViewModel: GymDetailViewModel = hiltViewModel(),
    classDetailViewModel: ClassDetailViewModel = hiltViewModel(),
    rootNavigationViewModel: RootNavigationViewModel = hiltViewModel(),
) {

    val rootNavigationUiState = rootNavigationViewModel.collectUiStateValue()
    val startDestination = rootNavigationUiState.startDestination

    val navController = rememberNavController()
    val systemUiController: SystemUiController = rememberSystemUiController()

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
        BottomNavScreens.Profile
        // TODO: Add new items when activities and profile are implemented.
    )

    systemUiController.setStatusBarColor(PRIMARY_YELLOW)

    //TODO: Try to consolidate launched effects into one with consumeIn function that takes in coroutine scope
    LaunchedEffect(rootNavigationUiState.navEvent) {
        rootNavigationUiState.navEvent?.consumeSuspend {
            navController.navigate(it)
        }
    }
    LaunchedEffect(rootNavigationUiState.popBackStack) {
        rootNavigationUiState.popBackStack?.consumeSuspend {
            navController.popBackStack()
        }
    }

    @Composable
    fun isRoute(route: UpliftRootRoute): Boolean {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        return currentDestination?.route == route::class.qualifiedName
    }

    Scaffold(bottomBar = {
        if (!isRoute(UpliftRootRoute.Onboarding)
            && !isRoute(UpliftRootRoute.ProfileCreation)
        ) {
            BottomNavigation(
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
        }
    }) {
        // TODO 1: Change back to home if releasing features before google sign in is finished.
        // TODO 2: Change to Onboarding after google sign in is finished
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(it)
        ) {
            composable<UpliftRootRoute.Home> {
                HomeScreen(
                    navController = navController,
                    openGym = gymDetailViewModel::openGym,
                    loadingShimmer = shimmer
                )
            }
            composable<UpliftRootRoute.GymDetail> {
                GymDetailScreen(
                    gymDetailViewModel = gymDetailViewModel,
                ) {
                    navController.popBackGym(gymDetailViewModel::popBackStack)
                }
            }
            composable<UpliftRootRoute.ClassDetail> {
                ClassDetailScreen(
                    classDetailViewModel = classDetailViewModel, navController = navController
                ) {
                    navController.popBackClass(classDetailViewModel::popBackStack)
                }
            }
            composable<UpliftRootRoute.Classes> {
                ClassScreen(
                    openClass = classDetailViewModel::openClass,
                    navController = navController,
                )
            }
            composable<UpliftRootRoute.ReportIssue> {
                ReportIssueScreen(
                    onBack = { navController.popBackStack() }
                )
            }
            composable<UpliftRootRoute.ReportSuccess> {
                ReportSubmittedScreen(
                    navigateToReport = { navController.navigate(UpliftRootRoute.ReportIssue) },
                    navigateToHome = { navController.navigate(UpliftRootRoute.Home) }
                )
            }
            composable<UpliftRootRoute.Onboarding> {
                SignInPromptScreen()
            }
            composable<UpliftRootRoute.ProfileCreation> {
                ProfileCreationScreen()
            }
            composable<UpliftRootRoute.Profile>{
                ProfileScreen()
            }
            composable<UpliftRootRoute.Reminders> {
                MainReminderScreen()
            }
            composable<UpliftRootRoute.Settings> {
                SettingsScreen()
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
    data object Reminders : UpliftRootRoute()

    @Serializable
    data object ReportIssue : UpliftRootRoute()

    @Serializable
    data object ReportSuccess : UpliftRootRoute()

    @Serializable
    data object Settings : UpliftRootRoute()
}
