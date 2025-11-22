package com.team.eatcleanapp.domain.usecase.health

import com.team.eatcleanapp.domain.model.ActivityLevel
import com.team.eatcleanapp.domain.model.Gender
import com.team.eatcleanapp.domain.model.Goal
import com.team.eatcleanapp.domain.model.HealthMetrics
import com.team.eatcleanapp.domain.model.User
import com.team.eatcleanapp.domain.repository.UserRepository
import com.team.eatcleanapp.util.NutritionCalculator
import com.team.eatcleanapp.util.Result

class UpdateHealthMetricsUseCase(
    private val userRepository: UserRepository
) {

    data class Params(
        val userId: String,
        val weightKg: Double,
        val heightCm: Double,
        val age: Int,
        val gender: Gender,
        val activityLevel: ActivityLevel,
        val goal: Goal
    )

    suspend operator fun invoke(params: Params): Result<User> {
        val currentUserResult = userRepository.getUserById(params.userId)
        if (currentUserResult is Result.Error) {
            return currentUserResult
        }

        val currentUser = (currentUserResult as Result.Success).data

        val bmi = NutritionCalculator.calculateBmi(
            weightKg = params.weightKg.toFloat(),
            heightCm = params.heightCm.toFloat()
        )

        val bmr = NutritionCalculator.calculateBmr(
            weightKg = params.weightKg.toFloat(),
            heightCm = params.heightCm.toFloat(),
            age = params.age,
            gender = params.gender
        )

        val tdee = NutritionCalculator.calculateTdee(
            bmr = bmr,
            activityLevel = params.activityLevel
        )

        val healthMetrics = HealthMetrics(
            bmi = bmi.toDouble(),
            bmr = bmr.toDouble(),
            tdee = tdee.toDouble()
        )

        val updatedUser = currentUser.copy(
            weight = params.weightKg,
            height = params.heightCm,
            age = params.age,
            gender = params.gender,
            activityLevel = params.activityLevel,
            goal = params.goal,
            healthMetrics = healthMetrics
        )

        return userRepository.updateUser(updatedUser)
    }
}