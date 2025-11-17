package com.team.eatcleanapp.domain.usecase.auth

import com.team.eatcleanapp.domain.model.User
import com.team.eatcleanapp.domain.repository.UserRepository
import com.team.eatcleanapp.util.Result
import com.team.eatcleanapp.util.Constants

class RegisterUseCase(
    private val repository: UserRepository
) {
    suspend fun register(
        user: User,
        confirmPassword: String
    ): Result<User> {
        if (user.email.isBlank() || user.password.isBlank() || confirmPassword.isBlank() || user.name.isBlank())
            return Result.Error(IllegalArgumentException("Vui long dien day du thong tin"))

        if (user.email.length < Constants.MIN_EMAIL_LENGTH || !user.email.contains("@"))
            return Result.Error(IllegalArgumentException("Email khong hop le"))

        if (user.password.length < Constants.MIN_PASSWORD_LENGTH)
            return Result.Error(IllegalArgumentException("Mat khau phai co it nhat ${Constants.MIN_PASSWORD_LENGTH} ky tu"))

        if (user.password != confirmPassword)
            return Result.Error(IllegalArgumentException("Mat khau xac nhan khong trung khop"))

        if (user.name.length > Constants.MAX_NAME_LENGTH)
            return Result.Error(IllegalArgumentException("Ten khong duoc qua ${Constants.MAX_NAME_LENGTH} ky tu"))

        val checkEmail = repository.checkEmailExists(user.email)
        if (checkEmail is Result.Error)
            return checkEmail
        if (checkEmail is Result.Success && checkEmail.data)
            return Result.Error(IllegalArgumentException("Email đã được sử dụng"))

        val userToRegister = user.copy(id = "")
        return repository.registerUser(userToRegister)
    }
}