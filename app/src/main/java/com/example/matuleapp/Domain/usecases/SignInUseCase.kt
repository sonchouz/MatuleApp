package com.example.matuleapp.Domain.usecase

import com.example.matuleapp.Domain.repository.AuthRepository

class SignInUseCase (private val repo: AuthRepository) {
    suspend operator fun invoke(email: String, password: String) =
        repo.signIn(email, password)
}
