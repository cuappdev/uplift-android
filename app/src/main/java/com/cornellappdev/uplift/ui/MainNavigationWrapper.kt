package com.cornellappdev.uplift.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.cornellappdev.uplift.nav.BottomNavScreen
import com.cornellappdev.uplift.nav.popBackClass
import com.cornellappdev.uplift.nav.popBackGym
import com.cornellappdev.uplift.networking.ApiResponse
import com.cornellappdev.uplift.ui.screens.ClassDetailScreen
import com.cornellappdev.uplift.ui.screens.ClassScreen
import com.cornellappdev.uplift.ui.screens.GymDetailScreen
import com.cornellappdev.uplift.ui.screens.HomeScreen
import com.cornellappdev.uplift.ui.screens.ProfileCreationScreen
import com.cornellappdev.uplift.ui.screens.SignInPromptScreen
import com.cornellappdev.uplift.ui.viewmodels.ClassDetailViewModel
import com.cornellappdev.uplift.ui.viewmodels.ClassesViewModel
import com.cornellappdev.uplift.ui.viewmodels.GymDetailViewModel
import com.cornellappdev.uplift.ui.viewmodels.HomeViewModel
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.PRIMARY_YELLOW
import com.cornellappdev.uplift.util.montserratFamily
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.defaultShimmerTheme
import com.valentinilk.shimmer.rememberShimmer

/**
 * The main navigation controller for the app.
 */
@Composable
fun MainNavigationWrapper(
    gymDetailViewModel: GymDetailViewModel = viewModel(),
    classDetailViewModel: ClassDetailViewModel = viewModel(),
    classesViewModel: ClassesViewModel = viewModel(),
    homeViewModel: HomeViewModel = viewModel()
) {
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
        BottomNavScreen.Home,
        BottomNavScreen.Classes,
        // TODO: Add new items when activities and profile are implemented.
    )

    systemUiController.setStatusBarColor(PRIMARY_YELLOW)

    navController.addOnDestinationChangedListener { controller, destination, arguments ->
        when (destination.route) {
            "homeMain" -> {
                homeViewModel.openHome()
            }
        }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (gymsState is ApiResponse.Success
                && (currentRoute != "signInPrompt")
                && (currentRoute != "profileCreation")
            )
                BottomNavigation(
                    backgroundColor = PRIMARY_YELLOW,
                    contentColor = PRIMARY_BLACK,
                    elevation = 1.dp
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    items.forEach { screen ->
                        val selected =
                            currentDestination?.hierarchy?.any { it.route == screen.route } == true
                        BottomNavigationItem(
                            icon = {
                                Icon(
                                    painter = painterResource(id = if (selected) screen.painterIds.second else screen.painterIds.first),
                                    contentDescription = null
                                )
                            },
                            label = {
                                Text(
                                    text = screen.titleText,
                                    fontFamily = montserratFamily,
                                    fontSize = 14.sp,
                                    fontWeight = if (selected) FontWeight(700) else FontWeight(500),
                                    lineHeight = 17.07.sp,
                                    textAlign = TextAlign.Center,
                                    overflow = TextOverflow.Visible,
                                    softWrap = false
                                )
                            },
                            selected = selected,
                            onClick = {
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
                            }
                        )
                    }
                }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = "onboarding",
            modifier = Modifier.padding(it)
        ) {
            navigation(startDestination = "homeMain", route = "home") {
                composable(route = "homeMain") {
                    HomeScreen(
                        homeViewModel = homeViewModel,
                        navController = navController,
                        classDetailViewModel = classDetailViewModel,
                        gymDetailViewModel = gymDetailViewModel,
                        loadingShimmer = shimmer
                    )
                }
                composable(route = "gymDetail") {
                    GymDetailScreen(
                        gymDetailViewModel = gymDetailViewModel,
                        navController = navController,
                        classDetailViewModel = classDetailViewModel
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
                composable(route = "classDetailHome") {
                    ClassDetailScreen(
                        classDetailViewModel = classDetailViewModel,
                        navController = navController
                    ) {
                        navController.popBackClass(classDetailViewModel)
                    }
                }
            }
            navigation(startDestination = "classesMain", route = "classes") {
                composable(route = "classesMain") {
                    ClassScreen(
                        classDetailViewModel = classDetailViewModel,
                        navController = navController,
                        classesViewModel = classesViewModel
                    )
                }
                composable(route = "classDetailClasses") {
                    ClassDetailScreen(
                        classDetailViewModel = classDetailViewModel,
                        navController = navController
                    ) {
                        navController.popBackClass(classDetailViewModel)
                    }
                }
            }
            navigation(startDestination = "sportsMain", route = "sports") {
                composable(route = "sportsMain") {}
            }
            navigation(startDestination = "favoritesMain", route = "favorites") {
                composable(route = "favoritesMain") {
                }
            }
            navigation(startDestination = "signInPrompt", route = "onboarding") {
                composable(route = "signInPrompt") {
                    SignInPromptScreen(navController = navController)
                }
                composable(route = "profileCreation") {
                    ProfileCreationScreen(navController = navController)
                }
            }


        }
    }
}
