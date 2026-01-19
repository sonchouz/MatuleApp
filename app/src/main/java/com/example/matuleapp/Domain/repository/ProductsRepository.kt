package com.example.matuleapp.Domain.repository

import com.example.matuleapp.Data.SupabaseProvider
import com.example.matuleapp.Data.models.ProductDto
import io.github.jan.supabase.postgrest.from

class ProductsRepository {
    suspend fun fetchProducts(): List<ProductDto> {

        return SupabaseProvider.supabase
            .from("Products")
            .select()

            .decodeList<ProductDto>()

    }
}