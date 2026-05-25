package com.example.outfitbase.data.repository

import com.example.outfitbase.data.local.cart.CartDao
import com.example.outfitbase.data.mapper.toCartItem
import com.example.outfitbase.data.mapper.toCartItemEntity
import com.example.outfitbase.domain.model.CartItem
import com.example.outfitbase.domain.repository.CartRepository
import com.example.outfitbase.util.InputSanitizer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomCartRepository(
    private val cartDao: CartDao
) : CartRepository {
    override fun getCartItems(): Flow<List<CartItem>> {
        return cartDao.observeCartItems().map { cartItems ->
            cartItems.map { cartItem -> cartItem.toCartItem() }
        }
    }

    override suspend fun addCartItem(cartItem: CartItem) {
        val entity = cartItem.toCartItemEntity()
        val existingItem = cartDao.getCartItem(
            productId = entity.productId,
            selectedSize = entity.selectedSize,
            selectedColor = entity.selectedColor
        )
        val quantity = entity.quantity + (existingItem?.quantity ?: 0)

        cartDao.upsertCartItem(entity.copy(quantity = quantity.coerceAtLeast(1)))
    }

    override suspend fun removeCartItem(productId: Int, selectedSize: String, selectedColor: String) {
        cartDao.removeCartItem(
            productId = productId,
            selectedSize = InputSanitizer.sanitizeStoredText(selectedSize, 20),
            selectedColor = InputSanitizer.sanitizeStoredText(selectedColor, 40)
        )
    }

    override suspend fun updateQuantity(
        productId: Int,
        selectedSize: String,
        selectedColor: String,
        quantity: Int
    ) {
        val sanitizedSize = InputSanitizer.sanitizeStoredText(selectedSize, 20)
        val sanitizedColor = InputSanitizer.sanitizeStoredText(selectedColor, 40)

        if (quantity <= 0) {
            cartDao.removeCartItem(productId, sanitizedSize, sanitizedColor)
            return
        }

        cartDao.updateQuantity(
            productId = productId,
            selectedSize = sanitizedSize,
            selectedColor = sanitizedColor,
            quantity = quantity
        )
    }

    override suspend fun clearCart() {
        cartDao.clearCart()
    }
}
