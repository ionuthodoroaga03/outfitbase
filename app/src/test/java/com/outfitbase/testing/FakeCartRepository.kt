package com.outfitbase.testing

import com.outfitbase.domain.model.CartItem
import com.outfitbase.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeCartRepository(
    initialItems: List<CartItem> = emptyList()
) : CartRepository {
    private val cartItems = MutableStateFlow(initialItems)

    override fun getCartItems(): Flow<List<CartItem>> {
        return cartItems
    }

    override suspend fun addCartItem(cartItem: CartItem) {
        cartItems.value = cartItems.value + cartItem
    }

    override suspend fun removeCartItem(productId: Int, selectedSize: String, selectedColor: String) {
        cartItems.value = cartItems.value.filterNot { cartItem ->
            cartItem.productId == productId &&
                cartItem.selectedSize == selectedSize &&
                cartItem.selectedColor == selectedColor
        }
    }

    override suspend fun updateQuantity(
        productId: Int,
        selectedSize: String,
        selectedColor: String,
        quantity: Int
    ) {
        cartItems.value = cartItems.value.mapNotNull { cartItem ->
            val matchesItem = cartItem.productId == productId &&
                cartItem.selectedSize == selectedSize &&
                cartItem.selectedColor == selectedColor

            when {
                !matchesItem -> cartItem
                quantity <= 0 -> null
                else -> cartItem.copy(quantity = quantity)
            }
        }
    }

    override suspend fun clearCart() {
        cartItems.value = emptyList()
    }
}
