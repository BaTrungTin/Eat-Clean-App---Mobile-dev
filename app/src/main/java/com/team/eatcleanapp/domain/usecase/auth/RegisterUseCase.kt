package com.team.eatcleanapp.domain.usecase.auth

import com.team.eatcleanapp.domain.model.user.User
import com.team.eatcleanapp.domain.repository.AuthRepository
import com.team.eatcleanapp.domain.repository.UserRepository
import com.team.eatcleanapp.domain.utils.EmailValidator
import com.team.eatcleanapp.util.Constants
import com.team.eatcleanapp.util.Result
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        name: String, email: String, password: String, confirmPassword: String
    ): Result<com.google.firebase.auth.FirebaseUser> {
        validateRegistrationData(name, email, password, confirmPassword)?.let {
            return Result.Error(message = it)
        }

        val emailCheck = authRepository.isEmailAlreadyInUse(email)
        if (emailCheck.isSuccess && emailCheck.getOrThrow() == true) {
            return Result.Error(message = "Email này đã được sử dụng")
        }

        val registerResult = authRepository.registerWithEmailAndPassword(email, password)
        if (registerResult.isError) return registerResult

        val firebaseUser = registerResult.getOrThrow()
        val user = User(id = firebaseUser.uid, email = email, name = name, createdAt = System.currentTimeMillis())

        val saveResult = userRepository.saveUser(user)
        return if (saveResult.isSuccess) {
            Result.Success(firebaseUser)
        } else {
            authRepository.deleteAccount()
            Result.Error(message = saveResult.errorMessage())
        }
    }

    private fun validateRegistrationData(
        name: String, email: String, password: String, confirmPassword: String
    ): String? = when {
        name.isBlank() -> "Họ tên không được để trống"
        email.isBlank() -> "Email không được để trống"
        password.isBlank() -> "Mật khẩu không được để trống"
        confirmPassword.isBlank() -> "Xác nhận mật khẩu không được để trống"
        password != confirmPassword -> "Mật khẩu xác nhận không khớp"
        password.length < Constants.MIN_PASSWORD_LENGTH -> "Mật khẩu phải có ít nhất ${Constants.MIN_PASSWORD_LENGTH} ký tự"
        !EmailValidator.isValid(email) -> "Email không đúng định dạng"
        else -> null
    }
}