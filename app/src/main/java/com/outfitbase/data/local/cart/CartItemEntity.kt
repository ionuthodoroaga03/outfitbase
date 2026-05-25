package com.outfitbase.data.local.cart

import androidx.room.Entity

@Entity(
    tableName = "cart_items",
    primaryKeys = ["productId", "selectedSize", "selectedColor"]
)
data class CartItemEntity(
    val productId: Int,
    val name: String,
    val price: Double,
    val imageUrl: String,
    val category: String,
    val quantity: Int,
    val selectedSize: String,
    val selectedColor: String
)
