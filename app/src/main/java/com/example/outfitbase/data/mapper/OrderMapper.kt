package com.example.outfitbase.data.mapper

import com.example.outfitbase.data.local.order.OrderEntity
import com.example.outfitbase.data.local.order.OrderItemEntity
import com.example.outfitbase.data.local.order.OrderWithItems
import com.example.outfitbase.domain.model.Order
import com.example.outfitbase.domain.model.OrderItem
import com.example.outfitbase.domain.model.OrderStatus
import com.example.outfitbase.util.InputSanitizer

fun OrderWithItems.toOrder(): Order {
    return Order(
        orderId = order.orderId,
        createdAt = order.createdAt,
        totalPrice = order.totalPrice,
        status = OrderStatus.valueOf(order.status),
        customerName = order.customerName,
        customerAddress = order.customerAddress,
        customerPhone = order.customerPhone,
        items = items.map { item -> item.toOrderItem() }
    )
}

fun Order.toOrderEntity(): OrderEntity {
    return OrderEntity(
        orderId = orderId,
        createdAt = InputSanitizer.sanitizeStoredText(createdAt, 40),
        totalPrice = totalPrice,
        status = status.name,
        customerName = InputSanitizer.sanitizeStoredText(customerName),
        customerAddress = InputSanitizer.sanitizeStoredText(customerAddress, 180),
        customerPhone = InputSanitizer.sanitizePhone(customerPhone)
    )
}

fun OrderItem.toOrderItemEntity(orderId: Long): OrderItemEntity {
    return OrderItemEntity(
        orderOwnerId = orderId,
        productId = productId,
        name = InputSanitizer.sanitizeStoredText(name),
        quantity = quantity.coerceAtLeast(1),
        selectedSize = InputSanitizer.sanitizeStoredText(selectedSize, 20),
        selectedColor = InputSanitizer.sanitizeStoredText(selectedColor, 40),
        unitPrice = unitPrice
    )
}

private fun OrderItemEntity.toOrderItem(): OrderItem {
    return OrderItem(
        productId = productId,
        name = name,
        quantity = quantity,
        selectedSize = selectedSize,
        selectedColor = selectedColor,
        unitPrice = unitPrice
    )
}
