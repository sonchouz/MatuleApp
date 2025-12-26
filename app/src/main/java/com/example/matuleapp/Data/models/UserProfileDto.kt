package com.example.matuleapp.Data.models

import kotlinx.serialization.Serializable

@Serializable
data class UserProfileDto(
    val id: Int,
    val email: String,
    val pswd: String
)

