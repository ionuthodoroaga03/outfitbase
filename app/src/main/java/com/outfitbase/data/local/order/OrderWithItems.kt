package com.outfitbase.data.local.order

import androidx.room.Embedded
import androidx.room.Relation

data class OrderWithItems(
    @Embedded val order: OrderEntity,
    @Relation(
        parentColumn = "orderId",
        entityColumn = "orderOwnerId"
    )
    val items: List<OrderItemEntity>
)
