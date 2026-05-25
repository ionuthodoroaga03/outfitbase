package com.outfitbase.domain.model

data class Order(
    val orderId: Long,
    val createdAt: String,
    val totalPrice: Double,
    val status: OrderStatus,
    val customerName: String,
    val customerAddress: String,
    val customerPhone: String,
    val items: List<OrderItem>
)

enum class OrderStatus {
    PENDING, CONFIRMED, CANCELLED
}

data class OrderItem(
    val productId: Int,
    val name: String,
    val quantity: Int,
    val selectedSize: String,
    val selectedColor: String,
    val unitPrice: Double
)
