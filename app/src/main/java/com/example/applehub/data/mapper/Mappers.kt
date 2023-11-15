package com.example.applehub.data.mapper

import com.example.applehub.data.model.entity.Favorite
import com.example.applehub.data.model.response.Product
import com.example.applehub.data.model.response.ProductListUI
import com.example.applehub.data.model.response.ProductUI

fun List<Product>.mapToProductListUI() =
    map {
        ProductListUI(
            id = it.id ?: 1,
            title = it.title.orEmpty(),
            price = it.price ?: 0.0,
            imageOne = it.imageOne.orEmpty(),
            isFavorite = it.isFavorite ?: false
        )
    }

fun Product.mapToProductUI() =
    ProductUI(
        id = id ?: 1,
        title = title.orEmpty(),
        price = price ?: 0.0,
        description = description.orEmpty(),
        category = category.orEmpty(),
        imageOne = imageOne.orEmpty(),
        imageTwo = imageTwo.orEmpty(),
        imageThree = imageThree.orEmpty(),
        rate = rate ?: 0.0,
        count = count ?: 0,
        saleState = saleState ?: false
    )

fun ProductUI.mapToProduct() =
    Product(
        id = id,
        title = title,
        price = price,
        description = description,
        category = category,
        imageOne = imageOne,
        imageTwo = imageTwo,
        imageThree = imageThree,
        rate = rate,
        count = count,
        saleState = saleState,
        isFavorite = isFavorite
    )

fun Product.mapToFavorite() =
    Favorite(
        id = id,
        title = title,
        price = price,
        description = description,
        category = category,
        imageOne = imageOne,
        imageTwo = imageTwo,
        imageThree = imageThree,
        rate = rate,
        count = count,
        saleState = saleState
    )

fun Favorite.mapToProduct() =
    Product(
        id = id,
        title = title,
        price = price,
        description = description,
        category = category,
        imageOne = imageOne,
        imageTwo = imageTwo,
        imageThree = imageThree,
        rate = rate,
        count = count,
        saleState = saleState,
        isFavorite = true
    )