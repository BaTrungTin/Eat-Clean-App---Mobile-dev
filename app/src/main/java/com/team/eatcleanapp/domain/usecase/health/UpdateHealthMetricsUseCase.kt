package com.team.eatcleanapp.domain.usecase.health

import com.team.eatcleanapp.domain.model.user.HealthMetrics
import com.team.eatcleanapp.domain.model.user.User
import com.team.eatcleanapp.domain.repository.UserRepository
import com.team.eatcleanapp.domain.utils.NutritionCalculator
import com.team.eatcleanapp.util.Result
import javax.inject.Inject

class UpdateHealthMetricsUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User): Result<User> {
        val healthMetrics = calculateHealthMetrics(user)
        val updatedUser = user.copy(
            healthMetrics = healthMetrics,
            updatedAt = System.currentTimeMillis()
        )

        return userRepository.updateUser(updatedUser).let { updateResult ->
            if (updateResult.isSuccess) {
                Result.Success(updatedUser)
            } else {
                Result.Error(message = updateResult.errorMessage())
            }
        }
    }

    private fun calculateHealthMetrics(user: User): HealthMetrics {
        val bmi = NutritionCalculator.calculateBmi(
            user.weight.toFloat(),
            user.height.toFloat())
        val bmr = NutritionCalculator.calculateBmr(
            user.weight.toFloat(),
            user.height.toFloat(), user.age, user.gender)
        val tdee = NutritionCalculator.calculateTdee(
            bmr,
            user.activityLevel)

        return HealthMetrics(
            bmi = bmi,
            bmr = bmr,
            tdee = tdee
        )
    }
}