package com.bnjdpn.petitesgouttes.ui.navigation

import android.app.Application
import androidx.compose.animation.*
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.bnjdpn.petitesgouttes.ui.screens.*

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Home)
    data object Freezer : Screen("freezer", "Cong\u00e9lateur", Icons.Default.Kitchen)
    data object History : Screen("history", "Historique", Icons.Default.History)
    data object Stats : Screen("stats", "Stats", Icons.Default.BarChart)
    data object Settings : Screen("settings", "Param\u00e8tres", Icons.Default.Settings)
    data object AddBag : Screen("add_bag", "Ajouter", Icons.Default.Add)
    data object EditBag : Screen("edit_bag/{bagId}", "Modifier", Icons.Default.Edit) {
        fun createRoute(bagId: Long) = "edit_bag/$bagId"
    }
}

val bottomNavScreens = listOf(Screen.Dashboard, Screen.Freezer, Screen.History, Screen.Stats)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in bottomNavScreens.map { it.route }
    val application = LocalContext.current.applicationContext as Application

    Scaffold(
        topBar = {
            if (showBottomBar) {
                TopAppBar(
                    title = { Text("Petites Gouttes") },
                    actions = {
                        IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                            Icon(Icons.Default.Settings, contentDescription = "Param\u00e8tres")
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavScreens.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(padding),
            enterTransition = { fadeIn() + slideInHorizontally { it / 4 } },
            exitTransition = { fadeOut() + slideOutHorizontally { -it / 4 } },
            popEnterTransition = { fadeIn() + slideInHorizontally { -it / 4 } },
            popExitTransition = { fadeOut() + slideOutHorizontally { it / 4 } }
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    onAddBag = { navController.navigate(Screen.AddBag.route) }
                )
            }

            composable(Screen.Freezer.route) {
                FreezerListScreen(
                    onAddBag = { navController.navigate(Screen.AddBag.route) },
                    onEditBag = { bagId -> navController.navigate(Screen.EditBag.createRoute(bagId)) }
                )
            }

            composable(Screen.History.route) {
                HistoryScreen()
            }

            composable(Screen.Stats.route) {
                StatsScreen()
            }

            composable(Screen.Settings.route) {
                SettingsScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.AddBag.route) {
                AddEditBagScreen(
                    onNavigateBack = { navController.popBackStack() },
                    application = application
                )
            }

            composable(
                route = Screen.EditBag.route,
                arguments = listOf(navArgument("bagId") { type = NavType.LongType })
            ) { backStackEntry ->
                val bagId = backStackEntry.arguments?.getLong("bagId")
                AddEditBagScreen(
                    bagId = bagId,
                    onNavigateBack = { navController.popBackStack() },
                    application = application
                )
            }
        }
    }
}
