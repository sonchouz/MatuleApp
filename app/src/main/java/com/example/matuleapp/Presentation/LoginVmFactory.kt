package com.example.matuleapp.Presentation
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.matuleapp.Data.repository.AuthRepositoryImpl
import com.example.matuleapp.Domain.usecase.GetMyProfileUseCase
import com.example.matuleapp.Domain.usecase.SignInUseCase
import com.example.matuleapp.Presentation.login.LoginViewModel

class LoginVmFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repo = AuthRepositoryImpl()
        return LoginViewModel(
            signIn = SignInUseCase(repo),
            getMyProfile = GetMyProfileUseCase(repo)
        ) as T
    }
}
