package com.outfitbase.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.outfitbase.domain.model.Product
import com.outfitbase.domain.repository.ProductRepository
import com.outfitbase.util.InputSanitizer
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SearchViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {
    private val queryChanges = MutableStateFlow("")
    private val categoryChanges = MutableStateFlow<String?>(null)
    private val _uiState = MutableStateFlow(SearchUiState(isLoading = true))

    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var products: List<Product> = emptyList()
    private var categories: List<String> = emptyList()
    private var hasLoadedProducts = false

    init {
        observeFilters()
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            runCatching {
                categories = productRepository.getCategories()
                products = productRepository.getProducts()
            }.onSuccess {
                hasLoadedProducts = true
                showFilteredProducts()
            }.onFailure { throwable ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = throwable.message ?: "Products could not be loaded."
                )
            }
        }
    }

    fun updateQuery(query: String) {
        val sanitizedQuery = InputSanitizer.sanitizeSearchQuery(query)
        queryChanges.value = sanitizedQuery
        _uiState.value = _uiState.value.copy(query = sanitizedQuery)
    }

    fun selectCategory(category: String?) {
        categoryChanges.value = category
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    @OptIn(FlowPreview::class)
    private fun observeFilters() {
        combine(
            queryChanges.debounce(300),
            categoryChanges
        ) { query, category -> query to category }
            .onEach { showFilteredProducts() }
            .launchIn(viewModelScope)
    }

    private fun showFilteredProducts() {
        if (!hasLoadedProducts) {
            return
        }

        val query = queryChanges.value.trim()
        val category = categoryChanges.value
        val filteredProducts = products.filter { product ->
            product.matchesQuery(query) && product.matchesCategory(category)
        }

        _uiState.value = SearchUiState(
            query = queryChanges.value,
            selectedCategory = category,
            categories = categories,
            products = filteredProducts,
            isLoading = false
        )
    }

    private fun Product.matchesQuery(query: String): Boolean {
        return query.isBlank() || name.contains(query, ignoreCase = true)
    }

    private fun Product.matchesCategory(category: String?): Boolean {
        return category == null || this.category == category
    }
}

class SearchViewModelFactory(
    private val productRepository: ProductRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(productRepository) as T
    }
}
