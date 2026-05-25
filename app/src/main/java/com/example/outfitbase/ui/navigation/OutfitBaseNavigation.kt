package com.example.outfitbase.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.outfitbase.ui.screens.cart.CartScreen
import com.example.outfitbase.ui.screens.checkout.CheckoutScreen
import com.example.outfitbase.ui.screens.home.HomeScreen
import com.example.outfitbase.ui.screens.order.OrderConfirmationScreen
import com.example.outfitbase.ui.screens.product.ProductDetailScreen
import com.example.outfitbase.ui.screens.profile.ProfileScreen
import com.example.outfitbase.ui.screens.search.SearchScreen
import com.example.outfitbase.ui.screens.settings.SettingsScreen

val bottomNavigationDestinations = listOf(
    BottomNavigationDestination(Route.Home, "Home", "H"),
    BottomNavigationDestination(Route.Search, "Search", "S"),
    BottomNavigationDestination(Route.Cart, "Cart", "C"),
    BottomNavigationDestination(Route.Profile, "Profile", "P")
)

@Composable
fun OutfitBaseNavHost(
    navController: NavHostController,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Route.Home.path,
        modifier = modifier.padding(innerPadding)
    ) {
        composable(Route.Home.path) {
            HomeScreen(
                onProductClick = { productId ->
                    navController.navigate(Route.ProductDetail.withId(productId))
                }
            )
        }
        composable(Route.Search.path) {
            SearchScreen(
                onProductClick = { productId ->
                    navController.navigate(Route.ProductDetail.withId(productId))
                }
            )
        }
        composable(Route.Cart.path) {
            CartScreen(
                onNavigateToCheckout = {
                    navController.navigate(Route.Checkout.path)
                }
            )
        }
        composable(Route.Profile.path) {
            ProfileScreen(
                onNavigateToSettings = {
                    navController.navigate(Route.Settings.path)
                }
            )
        }
        composable(
            route = Route.ProductDetail.path,
            arguments = listOf(
                navArgument(Route.ProductDetail.productIdArgument) {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt(Route.ProductDetail.productIdArgument)
            if (productId != null) {
                ProductDetailScreen(
                    productId = productId,
                    onNavigateBack = navController::popBackStack,
                    onAddToCart = {
                        navController.navigate(Route.Cart.path) {
                            popUpTo(Route.Home.path)
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
        composable(Route.Checkout.path) {
            CheckoutScreen(
                onOrderConfirmed = {
                    navController.navigate(Route.OrderConfirmation.path) {
                        popUpTo(Route.Cart.path) {
                            inclusive = true
                        }
                    }
                },
                onNavigateBack = navController::popBackStack
            )
        }
        composable(Route.OrderConfirmation.path) {
            OrderConfirmationScreen(
                onNavigateHome = {
                    navController.navigate(Route.Home.path) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Route.Settings.path) {
            SettingsScreen(
                onNavigateBack = navController::popBackStack
            )
        }
    }
}

@Composable
fun OutfitBaseNavigationBar(
    currentDestination: NavDestination?,
    onDestinationClick: (Route) -> Unit
) {
    NavigationBar {
        bottomNavigationDestinations.forEach { destination ->
            val isSelected = currentDestination.isSelected(destination.route)

            NavigationBarItem(
                selected = isSelected,
                onClick = { onDestinationClick(destination.route) },
                icon = { Text(destination.iconText) },
                label = { Text(destination.label) }
            )
        }
    }
}

private fun NavDestination?.isSelected(route: Route): Boolean {
    return this?.hierarchy?.any { destination -> destination.route == route.path } == true
}
