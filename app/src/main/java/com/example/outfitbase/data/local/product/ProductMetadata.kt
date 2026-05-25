package com.example.outfitbase.data.local.product

data class ProductMetadata(
    val stock: Int,
    val sizes: List<String>,
    val colors: List<String>
)
