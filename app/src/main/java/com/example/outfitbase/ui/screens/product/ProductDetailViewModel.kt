package com.example.outfitbase.ui.screens.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.outfitbase.domain.model.Product
import com.example.outfitbase.domain.repository.ProductRepository
import com.example.outfitbase.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val productId: Int,
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<Product>>(UiState.Loading)
    val uiState: StateFlow<UiState<Product>> = _uiState.asStateFlow()

    private val _quantity = MutableStateFlow(1)
    val quantity: StateFlow<Int> = _quantity.asStateFlow()

    private val _selectedSize = MutableStateFlow("")
    val selectedSize: StateFlow<String> = _selectedSize.asStateFlow()

    private val _selectedColor = MutableStateFlow("")
    val selectedColor: StateFlow<String> = _selectedColor.asStateFlow()

    private val _showAddedMessage = MutableStateFlow(false)
    val showAddedMessage: StateFlow<Boolean> = _showAddedMessage.asStateFlow()

    init {
        loadProduct()
    }

    fun loadProduct() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            runCatching {
                productRepository.getProduct(productId)
            }.onSuccess { product ->
                _selectedSize.value = product.sizes.firstOrNull().orEmpty()
                _selectedColor.value = product.colors.firstOrNull().orEmpty()
                _uiState.value = UiState.Success(product)
            }.onFailure { throwable ->
                _uiState.value = UiState.Error(throwable.message ?: "Product could not be loaded.")
            }
        }
    }

    fun selectSize(size: String) {
        _selectedSize.value = size
    }

    fun selectColor(color: String) {
        _selectedColor.value = color
    }

    fun increaseQuantity() {
        _quantity.value += 1
    }

    fun decreaseQuantity() {
        if (_quantity.value > 1) {
            _quantity.value -= 1
        }
    }

    fun markProductAdded() {
        _showAddedMessage.value = true
    }
}

class ProductDetailViewModelFactory(
    private val productId: Int,
    private val productRepository: ProductRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProductDetailViewModel(productId, productRepository) as T
    }
}
