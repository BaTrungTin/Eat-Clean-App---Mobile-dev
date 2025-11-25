package com.team.eatcleanapp.domain.usecase.user

import com.team.eatcleanapp.domain.model.enums.ActivityLevel
import com.team.eatcleanapp.domain.model.enums.Gender
import com.team.eatcleanapp.domain.model.enums.Goal
import com.team.eatcleanapp.domain.model.user.HealthMetrics
import com.team.eatcleanapp.domain.model.user.User
import com.team.eatcleanapp.domain.repository.UserRepository
import com.team.eatcleanapp.domain.utils.NutritionCalculator
import com.team.eatcleanapp.util.Result
import javax.inject.Inject

class CompleteUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        userId: String,
        name: String,
        weight: Double,
        height: Double,
        age: Int,
        gender: Gender,
        activityMinutesPerDay: Int,
        activityDaysPerWeek: Int,
        goal: Goal
    ): Result<User> {
        validateUserData(name, weight, height, age, activityMinutesPerDay, activityDaysPerWeek)?.let {
            return Result.Error(message = it)
        }

        val existingUser = userRepository.getUserById(userId).getOrNull()
        val totalMinutesPerWeek = activityMinutesPerDay * activityDaysPerWeek
        val activityLevel = NutritionCalculator.inferActivityLevel(totalMinutesPerWeek, age)
        val healthMetrics = calculateHealthMetrics(weight, height, age, gender, activityLevel)

        val user = existingUser?.copy(
            name = name, weight = weight, height = height, age = age, gender = gender,
            activityMinutesPerDay = activityMinutesPerDay, activityDaysPerWeek = activityDaysPerWeek,
            activityLevel = activityLevel, goal = goal, healthMetrics = healthMetrics,
            updatedAt = System.currentTimeMillis()
        ) ?: User(
            id = userId, email = "", name = name, weight = weight, height = height, age = age,
            gender = gender, activityMinutesPerDay = activityMinutesPerDay,
            activityDaysPerWeek = activityDaysPerWeek, activityLevel = activityLevel,
            goal = goal, healthMetrics = healthMetrics
        )

        val saveResult = userRepository.saveUser(user)
        return if (saveResult.isSuccess) {
            Result.Success(user)
        } else {
            Result.Error(message = saveResult.errorMessage())
        }
    }

    private fun validateUserData(
        name: String, weight: Double, height: Double, age: Int,
        activityMinutesPerDay: Int, activityDaysPerWeek: Int
    ): String? = when {
        name.isBlank() -> "Họ tên không được để trống"
        weight <= 0 -> "Cân nặng phải lớn hơn 0"
        height <= 0 -> "Chiều cao phải lớn hơn 0"
        age <= 0 -> "Tuổi phải lớn hơn 0"
        activityMinutesPerDay < 0 -> "Thời gian tập luyện không được âm"
        activityDaysPerWeek !in 0..7 -> "Số ngày tập không hợp lệ"
        else -> null
    }

    private fun calculateHealthMetrics(
        weight: Double, height: Double, age: Int, gender: Gender, activityLevel: ActivityLevel
    ): HealthMetrics {
        val bmi = NutritionCalculator.calculateBmi(weight.toFloat(), height.toFloat())
        val bmr = NutritionCalculator.calculateBmr(weight.toFloat(), height.toFloat(), age, gender)
        val tdee = NutritionCalculator.calculateTdee(bmr, activityLevel)
        return HealthMetrics(bmi = bmi, bmr = bmr, tdee = tdee)
    }
}