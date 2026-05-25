package com.outfitbase.data.local.product

object ProductMetadataCatalog {
    private val fallbackMetadata = ProductMetadata(
        stock = 10,
        sizes = listOf("S", "M", "L"),
        colors = listOf("Black", "White")
    )

    private val metadataByProductId = mapOf(
        1 to ProductMetadata(
            stock = 12,
            sizes = listOf("One Size"),
            colors = listOf("Green", "Black")
        ),
        2 to ProductMetadata(
            stock = 18,
            sizes = listOf("S", "M", "L", "XL"),
            colors = listOf("Black", "White", "Blue")
        ),
        3 to ProductMetadata(
            stock = 7,
            sizes = listOf("M", "L", "XL"),
            colors = listOf("Green", "Brown", "Navy")
        ),
        4 to ProductMetadata(
            stock = 15,
            sizes = listOf("S", "M", "L"),
            colors = listOf("Gray", "Black")
        ),
        15 to ProductMetadata(
            stock = 11,
            sizes = listOf("S", "M", "L"),
            colors = listOf("Black", "Red", "Gray")
        ),
        16 to ProductMetadata(
            stock = 8,
            sizes = listOf("S", "M", "L", "XL"),
            colors = listOf("Black", "Brown")
        ),
        17 to ProductMetadata(
            stock = 14,
            sizes = listOf("S", "M", "L"),
            colors = listOf("Blue", "Yellow", "White")
        ),
        18 to ProductMetadata(
            stock = 24,
            sizes = listOf("XS", "S", "M", "L"),
            colors = listOf("White", "Pink", "Beige")
        ),
        19 to ProductMetadata(
            stock = 20,
            sizes = listOf("S", "M", "L"),
            colors = listOf("Purple", "Blue", "White")
        ),
        20 to ProductMetadata(
            stock = 16,
            sizes = listOf("S", "M", "L", "XL"),
            colors = listOf("Red", "White", "Black")
        )
    )

    fun metadataFor(productId: Int): ProductMetadata {
        return metadataByProductId[productId] ?: fallbackMetadata
    }
}
