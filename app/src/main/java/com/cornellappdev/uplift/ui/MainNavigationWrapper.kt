package com.cornellappdev.uplift.ui

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.cornellappdev.uplift.nav.BottomNavScreen
import com.cornellappdev.uplift.nav.popBackClass
import com.cornellappdev.uplift.nav.popBackGym
import com.cornellappdev.uplift.networking.ClassWatcher
import com.cornellappdev.uplift.networking.GymWatcher
import com.cornellappdev.uplift.ui.screens.ClassDetailScreen
import com.cornellappdev.uplift.ui.screens.GymDetailScreen
import com.cornellappdev.uplift.ui.screens.HomeScreen
import com.cornellappdev.uplift.ui.viewmodels.ClassDetailViewModel
import com.cornellappdev.uplift.ui.viewmodels.GymDetailViewModel
import com.cornellappdev.uplift.ui.viewmodels.HomeViewModel
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.PRIMARY_YELLOW
import com.cornellappdev.uplift.util.montserratFamily
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * The main navigation controller for the app.
 */
@Composable
fun MainNavigationWrapper(
    gymDetailViewModel: GymDetailViewModel = viewModel(),
    classDetailViewModel: ClassDetailViewModel = viewModel(),
    homeViewModel: HomeViewModel = viewModel()
) {
    val navController = rememberNavController()
    val systemUiController: SystemUiController = rememberSystemUiController()

    GymWatcher(homeViewModel)
    ClassWatcher(homeViewModel)

    val items = listOf(
        BottomNavScreen.Home,
        BottomNavScreen.Classes,
        BottomNavScreen.Sports,
        BottomNavScreen.Favorites
    )

    systemUiController.setStatusBarColor(PRIMARY_YELLOW)

    navController.addOnDestinationChangedListener { controller, destination, arguments ->
        when (destination.route) {
            "homeMain" -> {
                homeViewModel.openHome()
            }
        }
    }

    Scaffold(
        bottomBar = {
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
                                textAlign = TextAlign.Center
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
        NavHost(navController = navController, startDestination = "home") {
            navigation(startDestination = "homeMain", route = "home") {
                composable(route = "homeMain") {
                    HomeScreen(
                        homeViewModel = homeViewModel,
                        navController = navController,
                        classDetailViewModel = classDetailViewModel,
                        gymDetailViewModel = gymDetailViewModel
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
                composable(route = "classDetail") {
                    ClassDetailScreen(
                        classDetailViewModel = classDetailViewModel,
                        navController = navController
                    ) {
                        navController.popBackClass(classDetailViewModel)
                    }
                }
            }
            navigation(startDestination = "classesMain", route = "classes") {
                composable(route = "classesMain") {}
            }
            navigation(startDestination = "sportsMain", route = "sports") {
                composable(route = "sportsMain") {}
            }
            navigation(startDestination = "favoritesMain", route = "favorites") {
                composable(route = "favoritesMain") {}
            }
        }
    }
}
