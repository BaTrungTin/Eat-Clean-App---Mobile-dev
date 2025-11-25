// GetNutritionProgressUseCase.kt - SỬA LẠI
package com.team.eatcleanapp.domain.usecase.mealintake

import com.team.eatcleanapp.domain.model.nutrition.NutritionInfo
import com.team.eatcleanapp.domain.repository.MealIntakeRepository
import com.team.eatcleanapp.util.Result
import java.util.Date
import javax.inject.Inject

class GetNutritionProgressUseCase @Inject constructor(
    private val mealIntakeRepository: MealIntakeRepository
) {
    suspend operator fun invoke(
        userId: String,
        date: Date,
        targetCalories: Double
    ): Result<NutritionInfo> {
        val consumedCaloriesResult = mealIntakeRepository.getTotalConsumedCalories(userId, date)
        val plannedCaloriesResult = mealIntakeRepository.getTotalPlannedCalories(userId, date)

        return when {
            consumedCaloriesResult.isError -> Result.Error(message = consumedCaloriesResult.errorMessage())
            plannedCaloriesResult.isError -> Result.Error(message = plannedCaloriesResult.errorMessage())
            else -> {
                val consumedCalories = consumedCaloriesResult.getOrThrow() ?: 0.0
                val plannedCalories = plannedCaloriesResult.getOrThrow()

                Result.Success(
                    NutritionInfo(
                        date = date,
                        targetCalories = targetCalories,
                        plannedCalories = plannedCalories,
                        consumedCalories = consumedCalories
                    )
                )
            }
        }
    }
}