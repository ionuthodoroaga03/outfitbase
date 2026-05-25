package com.example.outfitbase.di

import com.example.outfitbase.data.remote.NetworkModule
import com.example.outfitbase.data.repository.FakeStoreProductRepository
import com.example.outfitbase.domain.repository.ProductRepository

interface AppContainer {
    val productRepository: ProductRepository
}

class DefaultAppContainer : AppContainer {
    override val productRepository: ProductRepository by lazy {
        FakeStoreProductRepository(NetworkModule.fakeStoreApi)
    }
}
