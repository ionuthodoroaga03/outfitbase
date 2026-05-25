package com.example.outfitbase.ui.screens.checkout

import com.example.outfitbase.domain.model.CartItem

data class CheckoutUiState(
    val cartItems: List<CartItem> = emptyList(),
    val totalPrice: Double = 0.0,
    val customerName: String = "",
    val customerAddress: String = "",
    val customerPhone: String = "",
    val errorMessage: String? = null,
    val isSubmitting: Boolean = false
)
