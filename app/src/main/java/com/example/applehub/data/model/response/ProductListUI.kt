package com.example.applehub.data.model.response

data class ProductListUI(
    val id: Int,
    val title: String,
    val price: Double,
    val imageOne: String,
    val isFavorite: Boolean
)