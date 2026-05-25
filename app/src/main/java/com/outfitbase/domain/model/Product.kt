package com.outfitbase.domain.model

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val imageUrl: String,
    val rating: Double,
    val stock: Int,
    val sizes: List<String>,
    val colors: List<String>
)
