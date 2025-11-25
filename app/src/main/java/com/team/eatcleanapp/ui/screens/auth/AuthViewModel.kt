package com.team.eatcleanapp.ui.screens.auth

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.eatcleanapp.domain.model.User
import com.team.eatcleanapp.domain.usecase.auth.LoginUseCase
import com.team.eatcleanapp.domain.usecase.auth.RegisterUseCase
import com.team.eatcleanapp.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _authState = mutableStateOf<AuthState>(AuthState.Idle)
    val authState: State<AuthState> = _authState

    fun login(email: String, password: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                val result = loginUseCase.login(email, password)
                _authState.value = when (result) {
                    is Result.Success -> AuthState.LoginSuccess(result.data)
                    is Result.Error -> AuthState.Error(result.exception.message ?: "Đăng nhập thất bại")
                    is Result.Loading -> AuthState.Loading
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Lỗi không xác định: ${e.message}")
            }
        }
    }

    fun register(user: User, confirmPassword: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                val result = registerUseCase.register(user, confirmPassword)
                _authState.value = when (result) {
                    is Result.Success -> AuthState.RegisterSuccess(result.data)
                    is Result.Error -> AuthState.Error(result.exception.message ?: "Đăng ký thất bại")
                    is Result.Loading -> AuthState.Loading
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Lỗi không xác định: ${e.message}")
            }
        }
    }

    // Reset state về mặc định khi cần (ví dụ: sau khi hiển thị lỗi)
    fun resetState() {
        _authState.value = AuthState.Idle
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class LoginSuccess(val user: User) : AuthState()
    data class RegisterSuccess(val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
}
