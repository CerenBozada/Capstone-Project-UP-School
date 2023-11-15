package com.example.applehub.data.model.response

data class ProductUI(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val imageOne: String,
    val imageTwo: String,
    val imageThree: String,
    val rate: Double,
    val count: Int,
    val saleState: Boolean,
    val isFavorite: Boolean = false
) {
    companion object {
        val EMPTY: ProductUI
            get() = ProductUI(
                id = 0,
                title = "",
                price = 0.0,
                description = "",
                category = "",
                imageOne = "",
                imageTwo = "",
                imageThree = "",
                rate = 0.0,
                count = 0,
                saleState = false
            )
    }
}