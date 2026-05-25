package com.example.outfitbase.data.repository

import com.example.outfitbase.data.local.order.OrderDao
import com.example.outfitbase.data.mapper.toOrder
import com.example.outfitbase.data.mapper.toOrderEntity
import com.example.outfitbase.data.mapper.toOrderItemEntity
import com.example.outfitbase.domain.model.Order
import com.example.outfitbase.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomOrderRepository(
    private val orderDao: OrderDao
) : OrderRepository {
    override fun getOrders(): Flow<List<Order>> {
        return orderDao.observeOrders().map { orders ->
            orders.map { order -> order.toOrder() }
        }
    }

    override suspend fun saveOrder(order: Order) {
        val orderEntity = order.toOrderEntity()
        val persistedOrderId = if (order.orderId == 0L) {
            orderDao.upsertOrder(orderEntity)
        } else {
            orderDao.upsertOrder(orderEntity)
            order.orderId
        }
        val items = order.items.map { item -> item.toOrderItemEntity(persistedOrderId) }

        orderDao.deleteOrderItems(persistedOrderId)
        orderDao.insertOrderItems(items)
    }

    override suspend fun getOrder(orderId: Long): Order? {
        return orderDao.getOrder(orderId)?.toOrder()
    }
}
