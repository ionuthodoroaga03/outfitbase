package com.example.outfitbase.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.outfitbase.OutfitBaseApplication
import com.example.outfitbase.domain.model.ThemeMode
import com.example.outfitbase.ui.navigation.OutfitBaseNavHost
import com.example.outfitbase.ui.navigation.OutfitBaseNavigationBar
import com.example.outfitbase.ui.navigation.Route
import com.example.outfitbase.ui.navigation.bottomNavigationDestinations
import com.example.outfitbase.ui.theme.OutfitbaseTheme

@Composable
fun OutfitBaseApp() {
    val application = LocalContext.current.applicationContext as OutfitBaseApplication
    val settingsViewModel: AppSettingsViewModel = viewModel(
        factory = AppSettingsViewModelFactory(application.appContainer.settingsRepository)
    )
    val settings by settingsViewModel.settings.collectAsState()
    val systemDarkTheme = isSystemInDarkTheme()
    val darkTheme = when (settings.themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> systemDarkTheme
    }

    OutfitbaseTheme(darkTheme = darkTheme) {
        OutfitBaseScaffold()
    }
}

@Composable
private fun OutfitBaseScaffold() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val showBottomBar = bottomNavigationDestinations.any { destination ->
        destination.route.path == currentDestination?.route
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                OutfitBaseNavigationBar(
                    currentDestination = currentDestination,
                    onDestinationClick = { route ->
                        navController.navigateToBottomDestination(route)
                    }
                )
            }
        }
    ) { innerPadding ->
        OutfitBaseNavHost(
            navController = navController,
            innerPadding = innerPadding
        )
    }
}

private fun androidx.navigation.NavHostController.navigateToBottomDestination(route: Route) {
    navigate(route.path) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
