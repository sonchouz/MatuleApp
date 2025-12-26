package com.example.matuleapp.Domain.repository

import com.example.matuleapp.Domain.models.UserProfile

interface AuthRepository {
    suspend fun userExistsByEmail(email: String): Boolean
    suspend fun signIn(email: String, pswd: String)
    suspend fun signOut()
    suspend fun getMyProfile(): UserProfile
    fun isSignedIn(): Boolean
    suspend fun signUp(email: String, password: String)
}
