package com.example.matuleapp.Data.repository

import com.example.matuleapp.Data.SupabaseProvider
import com.example.matuleapp.Data.models.UserProfileDto
import com.example.matuleapp.Domain.models.UserProfile
import com.example.matuleapp.Domain.repository.AuthRepository
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest

class AuthRepositoryImpl : AuthRepository {

    private val supabase = SupabaseProvider.supabase

    override fun isSignedIn(): Boolean =
        supabase.auth.currentUserOrNull() != null

    override suspend fun userExistsByEmail(email: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun signIn(email: String, pswd: String) {
        supabase.auth.signInWith(Email) {
            this.email = email
            this.password = pswd
        }
    }

    override suspend fun signUp(email: String, password: String) {
        supabase.auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }

    }

    override suspend fun signOut() {
        supabase.auth.signOut()
    }

    override suspend fun getMyProfile(): UserProfile {
        val user = supabase.auth.currentUserOrNull()
            ?: error("No session. User not signed in.")

        val dto = supabase.postgrest["Users"]
            .select {
                filter { eq("id", user.id) }
            }
            .decodeSingle<UserProfileDto>()

        return dto.toDomain()
    }
}

private fun UserProfileDto.toDomain() = UserProfile(
    id = id,
    email = email,
    pswd = pswd
)
