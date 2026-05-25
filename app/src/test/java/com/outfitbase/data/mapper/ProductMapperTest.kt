package com.outfitbase.data.mapper

import com.outfitbase.data.remote.dto.ProductDto
import com.outfitbase.data.remote.dto.RatingDto
import org.junit.Assert.assertEquals
import org.junit.Test

class ProductMapperTest {
    @Test
    fun productDtoMapsToDomainProductWithLocalMetadata() {
        val productDto = ProductDto(
            id = 2,
            title = "  Mens Casual Premium Slim Fit T-Shirts  ",
            price = 22.3,
            description = "  Slim fit shirt  ",
            category = "  men's clothing  ",
            image = "  https://example.com/shirt.png  ",
            rating = RatingDto(rate = 4.1, count = 259)
        )

        val product = productDto.toProduct()

        assertEquals(2, product.id)
        assertEquals("Mens Casual Premium Slim Fit T-Shirts", product.name)
        assertEquals("Slim fit shirt", product.description)
        assertEquals("men's clothing", product.category)
        assertEquals("https://example.com/shirt.png", product.imageUrl)
        assertEquals(22.3, product.price, 0.0)
        assertEquals(4.1, product.rating, 0.0)
        assertEquals(18, product.stock)
        assertEquals(listOf("S", "M", "L", "XL"), product.sizes)
        assertEquals(listOf("Black", "White", "Blue"), product.colors)
    }
}
