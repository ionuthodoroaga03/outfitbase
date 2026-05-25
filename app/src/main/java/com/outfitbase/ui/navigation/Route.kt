package com.outfitbase.ui.navigation

sealed class Route(val path: String) {
    data object Splash : Route("splash")
    data object Home : Route("home")
    data object Search : Route("search")
    data object Cart : Route("cart")
    data object Profile : Route("profile")
    data object ProductDetail : Route("productDetail/{productId}") {
        const val productIdArgument = "productId"

        fun withId(id: Int) = "productDetail/$id"
    }
    data object Checkout : Route("checkout")
    data object OrderConfirmation : Route("orderConfirmation")
    data object Settings : Route("settings")
}
