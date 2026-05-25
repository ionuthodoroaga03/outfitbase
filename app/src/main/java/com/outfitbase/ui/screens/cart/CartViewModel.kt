package com.outfitbase.ui.screens.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.outfitbase.domain.model.CartItem
import com.outfitbase.domain.repository.CartRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartRepository: CartRepository
) : ViewModel() {
    val uiState: StateFlow<CartUiState> = cartRepository.getCartItems()
        .map { cartItems ->
            CartUiState(
                cartItems = cartItems,
                totalPrice = cartItems.sumOf { cartItem -> cartItem.lineTotal }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CartUiState()
        )

    fun increaseQuantity(cartItem: CartItem) {
        updateQuantity(cartItem, cartItem.quantity + 1)
    }

    fun decreaseQuantity(cartItem: CartItem) {
        updateQuantity(cartItem, cartItem.quantity - 1)
    }

    fun removeCartItem(cartItem: CartItem) {
        viewModelScope.launch {
            cartRepository.removeCartItem(
                productId = cartItem.productId,
                selectedSize = cartItem.selectedSize,
                selectedColor = cartItem.selectedColor
            )
        }
    }

    private fun updateQuantity(cartItem: CartItem, quantity: Int) {
        viewModelScope.launch {
            cartRepository.updateQuantity(
                productId = cartItem.productId,
                selectedSize = cartItem.selectedSize,
                selectedColor = cartItem.selectedColor,
                quantity = quantity
            )
        }
    }
}

class CartViewModelFactory(
    private val cartRepository: CartRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CartViewModel(cartRepository) as T
    }
}

private val CartItem.lineTotal: Double
    get() = price * quantity
