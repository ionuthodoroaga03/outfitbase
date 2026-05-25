package com.outfitbase.data.mapper

import com.outfitbase.data.local.product.ProductMetadataCatalog
import com.outfitbase.data.remote.dto.ProductDto
import com.outfitbase.domain.model.Product

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
