package com.example.matuleapp.Data.com.example.matuleapp.Domain

import com.example.matuleapp.Data.SupabaseProvider

class ProductsRepository {
    suspend fun fetchProducts(): List<ProductDto> {

        return SupabaseProvider.supabase
            .from("Products")
            .select()

            .decodeList<ProductDto>()

    }
}