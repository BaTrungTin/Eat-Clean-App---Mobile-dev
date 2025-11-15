package com.team.eatcleanapp.domain.usecase.auth

import com.team.eatcleanapp.domain.model.User
import com.team.eatcleanapp.domain.repository.UserRepository
import com.team.eatcleanapp.util.Result
import com.team.eatcleanapp.util.Constants

class LoginUseCase(
    private val repository: UserRepository
) {
    suspend fun login(
        email: String,
        password: String
    ): Result<User> {
        if (email.isBlank() || password.isBlank())
            return Result.Error(IllegalArgumentException("Email và mật khẩu không được để trống"))

        if (email.length < Constants.MIN_EMAIL_LENGTH || !email.contains("@"))
            return Result.Error(IllegalArgumentException("Email không hợp lệ"))

        if (password.length < Constants.MIN_PASSWORD_LENGTH)
            return Result.Error(IllegalArgumentException("Mật khẩu quá ngắn"))

        return repository.loginUser(email, password)
    }
}