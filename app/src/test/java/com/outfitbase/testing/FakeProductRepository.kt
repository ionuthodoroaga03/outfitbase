package com.outfitbase.testing

import com.outfitbase.domain.model.Product
import com.outfitbase.domain.repository.ProductRepository

class FakeProductRepository(
    private val products: List<Product> = emptyList(),
    private val categories: List<String> = emptyList(),
    private val error: Throwable? = null
) : ProductRepository {
    override suspend fun getProducts(): List<Product> {
        error?.let { throwable -> throw throwable }
        return products
    }

    override suspend fun getProduct(productId: Int): Product {
        error?.let { throwable -> throw throwable }
        return products.first { product -> product.id == productId }
    }

    override suspend fun getCategories(): List<String> {
        error?.let { throwable -> throw throwable }
        return categories
    }

    override suspend fun getProductsByCategory(category: String): List<Product> {
        error?.let { throwable -> throw throwable }
        return products.filter { product -> product.category == category }
    }
}
