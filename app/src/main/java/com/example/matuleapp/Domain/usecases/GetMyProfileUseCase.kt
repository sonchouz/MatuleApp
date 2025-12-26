package com.example.matuleapp.Domain.usecase

import com.example.matuleapp.Domain.models.UserProfile
import com.example.matuleapp.Domain.repository.AuthRepository

class GetMyProfileUseCase(private val repo: AuthRepository) {
    suspend operator fun invoke(): UserProfile = repo.getMyProfile()
}
