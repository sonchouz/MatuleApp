package com.example.matuleapp.Presentation.login

import com.example.matuleapp.Domain.models.UserProfile

sealed class LoginState {
    data object Idle : LoginState()
    data object Loading : LoginState()
    data class Success(val profile: UserProfile) : LoginState()
    data class Error(val message: String) : LoginState()
}
