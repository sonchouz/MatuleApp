package com.example.matuleapp.Data.com.example.matuleapp.Data

import kotlinx.serialization.SerialName

data class ProductDto(
    val id: Int,
    val title: String,
    val price: Int,
    @SerialName("imageurl") val imageurl: String,
    @SerialName ("badge")val badge: String? = null,
    val isfavorite: Boolean
)
