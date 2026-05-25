package com.outfitbase.testing

import com.outfitbase.domain.model.Product

fun testProduct(
    id: Int = 1,
    name: String = "Test Shirt",
    category: String = "men's clothing",
    price: Double = 10.0
): Product {
    return Product(
        id = id,
        name = name,
        description = "Test product",
        price = price,
        category = category,
        imageUrl = "https://example.com/product.png",
        rating = 4.5,
        stock = 5,
        sizes = listOf("S", "M", "L"),
        colors = listOf("Black", "White")
    )
}
