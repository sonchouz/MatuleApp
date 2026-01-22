package com.example.matuleapp.Data
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.createSupabaseClient

object SupabaseProvider {
    val supabase = createSupabaseClient(
        supabaseUrl = "https://ryibhjqukaxxzygyuhqw.supabase.co",
        supabaseKey = "sb_publishable_71n_erzX2rwouMOsUQD5bg_VJdoQOR5"
    ) {
        install(Postgrest)
    }
}



