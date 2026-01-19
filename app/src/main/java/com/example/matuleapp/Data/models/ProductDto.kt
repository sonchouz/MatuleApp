package com.example.matuleapp.Data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDto (
    val id: Int,
    val title: String,
    val price: Int,
    @SerialName("imageurl") val imageurl: String,
    @SerialName ("badge")val badge: String? = null
)
