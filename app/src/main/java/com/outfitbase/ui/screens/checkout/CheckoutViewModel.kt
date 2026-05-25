package com.outfitbase.ui.screens.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.outfitbase.domain.model.CartItem
import com.outfitbase.domain.model.Order
import com.outfitbase.domain.model.OrderItem
import com.outfitbase.domain.model.OrderStatus
import com.outfitbase.domain.repository.CartRepository
import com.outfitbase.domain.repository.OrderRepository
import com.outfitbase.util.InputSanitizer
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CheckoutViewModel(
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    init {
        observeCart()
    }

    fun updateCustomerName(customerName: String) {
        _uiState.value = _uiState.value.copy(
            customerName = customerName.take(120),
            errorMessage = null
        )
    }

    fun updateCustomerAddress(customerAddress: String) {
        _uiState.value = _uiState.value.copy(
            customerAddress = customerAddress.take(180),
            errorMessage = null
        )
    }

    fun updateCustomerPhone(customerPhone: String) {
        _uiState.value = _uiState.value.copy(
            customerPhone = InputSanitizer.sanitizePhone(customerPhone),
            errorMessage = null
        )
    }

    fun placeOrder(onOrderConfirmed: () -> Unit) {
        val state = uiState.value.sanitizedForStorage()
        val validationError = state.validationError()

        if (validationError != null) {
            _uiState.value = state.copy(errorMessage = validationError)
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isSubmitting = true, errorMessage = null)
            val order = state.toOrder()

            orderRepository.saveOrder(order)
            cartRepository.clearCart()
            _uiState.value = _uiState.value.copy(isSubmitting = false)
            onOrderConfirmed()
        }
    }

    private fun observeCart() {
        viewModelScope.launch {
            cartRepository.getCartItems().collectLatest { cartItems ->
                _uiState.value = _uiState.value.copy(
                    cartItems = cartItems,
                    totalPrice = cartItems.sumOf { cartItem -> cartItem.price * cartItem.quantity }
                )
            }
        }
    }

    private fun CheckoutUiState.validationError(): String? {
        return when {
            cartItems.isEmpty() -> "Cart is empty."
            customerName.isBlank() -> "Name is required."
            customerAddress.isBlank() -> "Address is required."
            customerPhone.length < 7 -> "Phone number is invalid."
            else -> null
        }
    }

    private fun CheckoutUiState.sanitizedForStorage(): CheckoutUiState {
        return copy(
            customerName = InputSanitizer.sanitizeStoredText(customerName),
            customerAddress = InputSanitizer.sanitizeStoredText(customerAddress, 180),
            customerPhone = InputSanitizer.sanitizePhone(customerPhone)
        )
    }

    private fun CheckoutUiState.toOrder(): Order {
        return Order(
            orderId = 0,
            createdAt = currentTimestamp(),
            totalPrice = totalPrice,
            status = OrderStatus.CONFIRMED,
            customerName = customerName,
            customerAddress = customerAddress,
            customerPhone = customerPhone,
            items = cartItems.map { cartItem -> cartItem.toOrderItem() }
        )
    }

    private fun CartItem.toOrderItem(): OrderItem {
        return OrderItem(
            productId = productId,
            name = name,
            quantity = quantity,
            selectedSize = selectedSize,
            selectedColor = selectedColor,
            unitPrice = price
        )
    }

    private fun currentTimestamp(): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date())
    }
}

class CheckoutViewModelFactory(
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CheckoutViewModel(cartRepository, orderRepository) as T
    }
}
