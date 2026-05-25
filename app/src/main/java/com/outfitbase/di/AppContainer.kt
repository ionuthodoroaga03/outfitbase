package com.outfitbase.di

import android.content.Context
import com.outfitbase.data.local.OutfitBaseDatabase
import com.outfitbase.data.remote.NetworkModule
import com.outfitbase.data.repository.DataStoreSettingsRepository
import com.outfitbase.data.repository.DataStoreUserProfileRepository
import com.outfitbase.data.repository.FakeStoreProductRepository
import com.outfitbase.data.repository.RoomCartRepository
import com.outfitbase.data.repository.RoomOrderRepository
import com.outfitbase.domain.repository.CartRepository
import com.outfitbase.domain.repository.OrderRepository
import com.outfitbase.domain.repository.ProductRepository
import com.outfitbase.domain.repository.SettingsRepository
import com.outfitbase.domain.repository.UserProfileRepository

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
