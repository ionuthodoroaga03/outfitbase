package com.example.outfitbase.di

import android.content.Context
import com.example.outfitbase.data.local.OutfitBaseDatabase
import com.example.outfitbase.data.remote.NetworkModule
import com.example.outfitbase.data.repository.DataStoreSettingsRepository
import com.example.outfitbase.data.repository.DataStoreUserProfileRepository
import com.example.outfitbase.data.repository.FakeStoreProductRepository
import com.example.outfitbase.data.repository.RoomCartRepository
import com.example.outfitbase.data.repository.RoomOrderRepository
import com.example.outfitbase.domain.repository.CartRepository
import com.example.outfitbase.domain.repository.OrderRepository
import com.example.outfitbase.domain.repository.ProductRepository
import com.example.outfitbase.domain.repository.SettingsRepository
import com.example.outfitbase.domain.repository.UserProfileRepository

interface AppContainer {
    val productRepository: ProductRepository
    val cartRepository: CartRepository
    val orderRepository: OrderRepository
    val settingsRepository: SettingsRepository
    val userProfileRepository: UserProfileRepository
}

class DefaultAppContainer(context: Context) : AppContainer {
    private val database: OutfitBaseDatabase by lazy {
        OutfitBaseDatabase.getInstance(context)
    }

    override val productRepository: ProductRepository by lazy {
        FakeStoreProductRepository(NetworkModule.fakeStoreApi)
    }

    override val cartRepository: CartRepository by lazy {
        RoomCartRepository(database.cartDao())
    }

    override val orderRepository: OrderRepository by lazy {
        RoomOrderRepository(database.orderDao())
    }

    override val settingsRepository: SettingsRepository by lazy {
        DataStoreSettingsRepository(context)
    }

    override val userProfileRepository: UserProfileRepository by lazy {
        DataStoreUserProfileRepository(context)
    }
}
