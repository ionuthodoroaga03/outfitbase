package com.outfitbase.domain.repository

import com.outfitbase.domain.model.Product

interface ProductRepository {
    suspend fun getProducts(): List<Product>

    suspend fun getProduct(productId: Int): Product

    suspend fun getCategories(): List<String>

    suspend fun getProductsByCategory(category: String): List<Product>
}
