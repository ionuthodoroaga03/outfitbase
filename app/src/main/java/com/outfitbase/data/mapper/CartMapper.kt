package com.outfitbase.data.mapper

import com.outfitbase.data.local.cart.CartItemEntity
import com.outfitbase.domain.model.CartItem
import com.outfitbase.util.InputSanitizer

fun CartItemEntity.toCartItem(): CartItem {
    return CartItem(
        productId = productId,
        name = name,
        price = price,
        imageUrl = imageUrl,
        category = category,
        quantity = quantity,
        selectedSize = selectedSize,
        selectedColor = selectedColor
    )
}

fun CartItem.toCartItemEntity(): CartItemEntity {
    return CartItemEntity(
        productId = productId,
        name = InputSanitizer.sanitizeStoredText(name),
        price = price,
        imageUrl = imageUrl.trim(),
        category = InputSanitizer.sanitizeStoredText(category),
        quantity = quantity.coerceAtLeast(1),
        selectedSize = InputSanitizer.sanitizeStoredText(selectedSize, 20),
        selectedColor = InputSanitizer.sanitizeStoredText(selectedColor, 40)
    )
}
