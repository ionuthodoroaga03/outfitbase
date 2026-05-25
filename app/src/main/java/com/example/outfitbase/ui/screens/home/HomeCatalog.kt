package com.example.outfitbase.ui.screens.home

import com.example.outfitbase.domain.model.Product

data class HomeCatalog(
    val products: List<Product>,
    val categories: List<String>,
    val selectedCategory: String?
)
