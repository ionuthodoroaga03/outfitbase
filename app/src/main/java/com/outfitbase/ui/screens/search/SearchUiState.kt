package com.outfitbase.ui.screens.search

import com.outfitbase.domain.model.Product

data class SearchUiState(
    val query: String = "",
    val selectedCategory: String? = null,
    val categories: List<String> = emptyList(),
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
