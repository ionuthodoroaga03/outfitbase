package com.outfitbase.domain.repository

import com.outfitbase.domain.model.Order
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getOrders(): Flow<List<Order>>

    suspend fun saveOrder(order: Order)

    suspend fun getOrder(orderId: Long): Order?
}
