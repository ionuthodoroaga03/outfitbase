package com.outfitbase.data.repository

import com.outfitbase.data.mapper.toProduct
import com.outfitbase.data.remote.FakeStoreApi
import com.outfitbase.domain.model.Product
import com.outfitbase.domain.repository.ProductRepository

class FakeStoreProductRepository(
    private val fakeStoreApi: FakeStoreApi
) : ProductRepository {
    override suspend fun getProducts(): List<Product> {
        return fakeStoreApi.getProducts().map { productDto -> productDto.toProduct() }
    }

    override suspend fun getProduct(productId: Int): Product {
        return fakeStoreApi.getProduct(productId).toProduct()
    }

    override suspend fun getCategories(): List<String> {
        return fakeStoreApi.getCategories()
    }

    override suspend fun getProductsByCategory(category: String): List<Product> {
        return fakeStoreApi.getProductsByCategory(category).map { productDto -> productDto.toProduct() }
    }
}
