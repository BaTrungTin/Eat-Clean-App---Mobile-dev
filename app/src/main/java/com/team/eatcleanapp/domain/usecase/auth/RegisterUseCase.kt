package com.team.eatcleanapp.domain.usecase.auth

import com.team.eatcleanapp.domain.model.User
import com.team.eatcleanapp.domain.repository.AuthRepository
import com.team.eatcleanapp.util.Result
import com.team.eatcleanapp.util.Constants
import com.team.eatcleanapp.domain.model.*

class RegisterUseCase(
    private val authRepository: AuthRepository
) {
    suspend fun register(
        email: String,
        password: String,
        confirmPassword: String,
        name: String
    ): Result<User> {
        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank() || name.isBlank())
            return Result.Error(IllegalArgumentException("Vui lòng điền đầy đủ thông tin"))

        if (email.length < Constants.MIN_EMAIL_LENGTH || !email.contains("@"))
            return Result.Error(IllegalArgumentException("Email không hợp lệ"))

        if (password.length < Constants.MIN_PASSWORD_LENGTH)
            return Result.Error(IllegalArgumentException("Mật khẩu phải có ít nhất ${Constants.MIN_PASSWORD_LENGTH} ký tự"))

        if (password != confirmPassword)
            return Result.Error(IllegalArgumentException("Mật khẩu xác nhận không trùng khớp"))

        if (name.length > Constants.MAX_NAME_LENGTH)
            return Result.Error(IllegalArgumentException("Tên không được quá ${Constants.MAX_NAME_LENGTH} ký tự"))

        // Gọi authRepository để đăng ký
        val registerResult = authRepository.registerUser(email, password)

        return when (registerResult) {
            is Result.Success -> {
                val userId = registerResult.data
                // Tạo user object với thông tin đầy đủ
                val user = User(
                    id = userId,
                    email = email,
                    name = name,
                    weight = 0.0,
                    height = 0.0,
                    age = 0,
                    gender = Gender.MALE,
                    activityMinutesPerDay = 0,
                    activityDaysPerWeek = 0,
                    activityLevel = ActivityLevel.SEDENTARY,
                    goal = Goal.MAINTAIN_WEIGHT,
                    healthMetrics = null
                )
                // Hoàn thành đăng ký với thông tin user
                authRepository.completeRegistration(user)
            }
            is Result.Error -> registerResult
            is Result.Loading -> Result.Error(IllegalStateException("Đang xử lý đăng ký"))
        }
    }
}