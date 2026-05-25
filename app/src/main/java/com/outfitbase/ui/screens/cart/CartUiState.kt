package com.outfitbase.ui.screens.cart

import com.outfitbase.domain.model.CartItem

data class CartUiState(
    val cartItems: List<CartItem> = emptyList(),
    val totalPrice: Double = 0.0
)
