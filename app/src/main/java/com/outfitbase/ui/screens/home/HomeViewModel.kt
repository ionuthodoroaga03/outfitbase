package com.outfitbase.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.outfitbase.domain.repository.ProductRepository
import com.outfitbase.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<HomeCatalog>>(UiState.Loading)
    val uiState: StateFlow<UiState<HomeCatalog>> = _uiState.asStateFlow()

    private var categories: List<String> = emptyList()

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            runCatching {
                categories = productRepository.getCategories()
                HomeCatalog(
                    products = productRepository.getProducts(),
                    categories = categories,
                    selectedCategory = null
                )
            }.onSuccess { catalog ->
                _uiState.value = UiState.Success(catalog)
            }.onFailure { throwable ->
                _uiState.value = UiState.Error(throwable.message ?: "Products could not be loaded.")
            }
        }
    }

    fun selectCategory(category: String?) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            runCatching {
                val products = if (category == null) {
                    productRepository.getProducts()
                } else {
                    productRepository.getProductsByCategory(category)
                }
                HomeCatalog(
                    products = products,
                    categories = categories,
                    selectedCategory = category
                )
            }.onSuccess { catalog ->
                _uiState.value = UiState.Success(catalog)
            }.onFailure { throwable ->
                _uiState.value = UiState.Error(throwable.message ?: "Products could not be loaded.")
            }
        }
    }
}

class HomeViewModelFactory(
    private val productRepository: ProductRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(productRepository) as T
    }
}
