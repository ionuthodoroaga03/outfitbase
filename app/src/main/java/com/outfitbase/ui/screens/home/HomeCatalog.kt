package com.outfitbase.ui.screens.home

import com.outfitbase.domain.model.Product

data class HomeCatalog(
    val products: List<Product>,
    val categories: List<String>,
    val selectedCategory: String?
)
