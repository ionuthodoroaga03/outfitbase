package com.outfitbase.domain.repository

import com.outfitbase.domain.model.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getCartItems(): Flow<List<CartItem>>

    suspend fun addCartItem(cartItem: CartItem)

    suspend fun removeCartItem(productId: Int, selectedSize: String, selectedColor: String)

    suspend fun updateQuantity(productId: Int, selectedSize: String, selectedColor: String, quantity: Int)

    suspend fun clearCart()
}
