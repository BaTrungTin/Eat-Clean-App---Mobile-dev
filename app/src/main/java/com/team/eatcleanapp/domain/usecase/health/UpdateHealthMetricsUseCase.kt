package com.team.eatcleanapp.domain.usecase.health

import com.team.eatcleanapp.domain.model.ActivityLevel
import com.team.eatcleanapp.domain.model.Gender
import com.team.eatcleanapp.domain.model.Goal
import com.team.eatcleanapp.domain.model.HealthMetrics
import com.team.eatcleanapp.domain.model.User
import com.team.eatcleanapp.domain.repository.UserRepository
import com.team.eatcleanapp.util.NutritionCalculator
import com.team.eatcleanapp.util.Result

// Use case cập nhật lại các chỉ số sức khỏe (BMI, BMR, TDEE) cho user
class UpdateHealthMetricsUseCase(
    private val userRepository: UserRepository
) {

    // tạo 1 data class để chứa các tham số cần thiết để cập nhật chỉ số sức khỏe
    data class Params(
        val userId: String,
        val weightKg: Float,
        val heightCm: Float,
        val age: Int,
        val gender: Gender,
        val activityLevel: ActivityLevel,
        val goal: Goal
    )

    // Trả về Result<User> cho đúng với UserRepository.updateUser
    suspend operator fun invoke(params: Params): Result<User> {

        // 1. Lấy user hiện tại theo id
        val currentUserResult = userRepository.getUserById(params.userId)

        // Nếu không lấy được thì trả về lỗi
        if (currentUserResult is Result.Error) {
            return Result.Error(currentUserResult.exception)
        }

        val currentUser = (currentUserResult as Result.Success).data

        // 2. Tính toán BMI, BMR, TDEE
        val bmi = NutritionCalculator.calculateBmi(
            weightKg = params.weightKg,
            heightCm = params.heightCm
        )

        val bmr = NutritionCalculator.calculateBmr(
            weightKg = params.weightKg,
            heightCm = params.heightCm,
            age = params.age,
            gender = params.gender
        )

        val tdee = NutritionCalculator.calculateTdee(
            bmr = bmr,
            activityLevel = params.activityLevel
        )

        // 3. Tạo HealthMetrics mới và cập nhật vào user
        val healthMetrics = HealthMetrics(
            bmi = bmi,
            bmr = bmr,
            tdee = tdee
        )

        // Tạo user đã được cập nhật chỉ số sức khỏe
        val updatedUser: User = currentUser.copy(
            weight = params.weightKg.toDouble(),
            height = params.heightCm.toDouble(),
            age = params.age,
            gender = params.gender,
            activityLevel = params.activityLevel,
            goal = params.goal,
            healthMetrics = healthMetrics
        )

        // Cập nhật user
        return userRepository.updateUser(updatedUser)
    }
}
