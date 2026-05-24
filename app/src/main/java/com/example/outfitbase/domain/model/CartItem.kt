package com.example.outfitbase.domain.model

data class CartItem(
    val productId: Int,
    val name: String,
    val price: Double,
    val imageUrl: String,
    val category: String,
    val quantity: Int,
    val selectedSize: String,
    val selectedColor: String
)
