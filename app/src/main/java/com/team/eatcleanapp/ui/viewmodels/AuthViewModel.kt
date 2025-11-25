package com.team.eatcleanapp.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.eatcleanapp.domain.usecase.auth.*
import com.team.eatcleanapp.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val sendPasswordResetEmailUseCase: SendPasswordResetEmailUseCase,
    private val checkEmailAvailabilityUseCase: CheckEmailAvailabilityUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase
) : ViewModel() {

    private val _loginState = MutableStateFlow<Result<*>>(Result.Idle)
    val loginState: StateFlow<Result<*>> = _loginState

    private val _registerState = MutableStateFlow<Result<*>>(Result.Idle)
    val registerState: StateFlow<Result<*>> = _registerState

    private val _passwordResetState = MutableStateFlow<Result<Unit>>(Result.Idle)
    val passwordResetState: StateFlow<Result<Unit>> = _passwordResetState

    private val _emailCheckState = MutableStateFlow<Result<Boolean>>(Result.Idle)
    val emailCheckState: StateFlow<Result<Boolean>> = _emailCheckState

    private val _logoutState = MutableStateFlow<Result<Unit>>(Result.Idle)
    val logoutState: StateFlow<Result<Unit>> = _logoutState

    private val _deleteAccountState = MutableStateFlow<Result<Unit>>(Result.Idle)
    val deleteAccountState: StateFlow<Result<Unit>> = _deleteAccountState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = Result.Loading
            _loginState.value = loginUseCase(email, password)
        }
    }

    fun register(name: String, email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            _registerState.value = Result.Loading
            _registerState.value = registerUseCase(name, email, password, confirmPassword)
        }
    }

    fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            _passwordResetState.value = Result.Loading
            _passwordResetState.value = sendPasswordResetEmailUseCase(email)
        }
    }

    fun checkEmailAvailability(email: String) {
        viewModelScope.launch {
            _emailCheckState.value = Result.Loading
            _emailCheckState.value = checkEmailAvailabilityUseCase(email)
        }
    }

    fun logout() {
        viewModelScope.launch {
            _logoutState.value = Result.Loading
            _logoutState.value = logoutUseCase()
        }
    }

    fun deleteAccount(userId: String) {
        viewModelScope.launch {
            _deleteAccountState.value = Result.Loading
            _deleteAccountState.value = deleteAccountUseCase(userId)
        }
    }

    fun resetLoginState() {
        _loginState.value = Result.Idle
    }

    fun resetRegisterState() {
        _registerState.value = Result.Idle
    }

    fun resetPasswordResetState() {
        _passwordResetState.value = Result.Idle
    }

    fun resetEmailCheckState() {
        _emailCheckState.value = Result.Idle
    }

    fun resetLogoutState() {
        _logoutState.value = Result.Idle
    }

    fun resetDeleteAccountState() {
        _deleteAccountState.value = Result.Idle
    }
}