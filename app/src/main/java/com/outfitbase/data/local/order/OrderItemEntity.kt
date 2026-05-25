package com.outfitbase.data.local.order

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "order_items",
    foreignKeys = [
        ForeignKey(
            entity = OrderEntity::class,
            parentColumns = ["orderId"],
            childColumns = ["orderOwnerId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("orderOwnerId")]
)
data class OrderItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val orderOwnerId: Long,
    val productId: Int,
    val name: String,
    val quantity: Int,
    val selectedSize: String,
    val selectedColor: String,
    val unitPrice: Double
)
