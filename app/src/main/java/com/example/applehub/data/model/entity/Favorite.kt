package com.example.applehub.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class Favorite(
    @PrimaryKey(autoGenerate = false)
    val id: Int?,
    val title: String?,
    val price: Double?,
    val description: String?,
    val category: String?,
    val imageOne: String?,
    val imageTwo: String?,
    val imageThree: String?,
    val rate: Double?,
    val count: Int?,
    val saleState: Boolean?,
)
