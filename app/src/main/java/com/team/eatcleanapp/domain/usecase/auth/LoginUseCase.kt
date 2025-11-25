package com.team.eatcleanapp.domain.usecase.auth

import com.team.eatcleanapp.domain.repository.AuthRepository
import com.team.eatcleanapp.domain.utils.EmailValidator
import com.team.eatcleanapp.util.Constants
import com.team.eatcleanapp.util.Result
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<com.google.firebase.auth.FirebaseUser> {
        if (email.isBlank())
            return Result.Error(message = "Email không được để trống")
        if (password.isBlank())
            return Result.Error(message = "Mật khẩu không được để trống")
        if (password.length < Constants.MIN_PASSWORD_LENGTH) {
            return Result.Error(message = "Mật khẩu phải có ít nhất ${Constants.MIN_PASSWORD_LENGTH} ký tự")
        }
        if (!EmailValidator.isValid(email))
            return Result.Error(message = "Email không đúng định dạng")

        return authRepository.loginWithEmailAndPassword(email, password)
    }
}