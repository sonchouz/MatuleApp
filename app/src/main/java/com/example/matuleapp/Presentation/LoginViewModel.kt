package com.example.matuleapp.Presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matuleapp.Domain.usecase.GetMyProfileUseCase
import com.example.matuleapp.Domain.usecase.SignInUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val signIn: SignInUseCase,
    private val getMyProfile: GetMyProfileUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<LoginState>(LoginState.Idle)
    val state: StateFlow<LoginState> = _state

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _state.value = LoginState.Error("Заполни email и пароль. Да, оба.")
            return
        }

        viewModelScope.launch {
            _state.value = LoginState.Loading
            runCatching {
                signIn(email.trim(), password)
                getMyProfile()
            }.onSuccess { profile ->
                _state.value = LoginState.Success(profile)
            }.onFailure { e ->
                _state.value = LoginState.Error(e.message ?: "Ошибка входа")
            }
        }
    }
}
