package com.example.outfitbase.data.local.order

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val orderId: Long = 0,
    val createdAt: String,
    val totalPrice: Double,
    val status: String,
    val customerName: String,
    val customerAddress: String,
    val customerPhone: String
)
