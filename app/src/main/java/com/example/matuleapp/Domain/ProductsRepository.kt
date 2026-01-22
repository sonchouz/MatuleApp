package com.example.matuleapp.Data.com.example.matuleapp.Domain

import com.example.matuleapp.Data.SupabaseProvider
import com.example.matuleapp.Data.com.example.matuleapp.Data.ProductDto
import io.github.jan.supabase.postgrest.from

class ProductsRepository {
    suspend fun fetchProducts(): List<ProductDto> {

        return SupabaseProvider.supabase
            .from("Products")
            .select()

            .decodeList<ProductDto>()

    }
}


