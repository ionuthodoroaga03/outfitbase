package com.example.outfitbase.data.mapper

import com.example.outfitbase.data.local.product.ProductMetadataCatalog
import com.example.outfitbase.data.remote.dto.ProductDto
import com.example.outfitbase.domain.model.Product

fun ProductDto.toProduct(): Product {
    val metadata = ProductMetadataCatalog.metadataFor(id)

    return Product(
        id = id,
        name = title.trim(),
        description = description.trim(),
        price = price,
        category = category.trim(),
        imageUrl = image.trim(),
        rating = rating.rate,
        stock = metadata.stock,
        sizes = metadata.sizes,
        colors = metadata.colors
    )
}
