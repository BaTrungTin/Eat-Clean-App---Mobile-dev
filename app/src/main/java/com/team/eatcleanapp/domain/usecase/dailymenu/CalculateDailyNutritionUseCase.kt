package com.team.eatcleanapp.domain.usecase.dailymenu

import com.team.eatcleanapp.domain.model.dailymenu.DailyMenuDay
import com.team.eatcleanapp.domain.model.nutrition.NutritionInfo
import com.team.eatcleanapp.domain.repository.DailyMenuRepository
import com.team.eatcleanapp.domain.repository.MealIntakeRepository
import com.team.eatcleanapp.util.Result
import java.util.Date
import javax.inject.Inject

class CalculateDailyNutritionUseCase @Inject constructor(
    private val dailyMenuRepository: DailyMenuRepository,
    private val mealIntakeRepository: MealIntakeRepository
) {
    suspend operator fun invoke(
        userId: String,
        date: Date,
        targetCalories: Double
    ): Result<NutritionInfo> {
        val plannedCaloriesResult = dailyMenuRepository.getTotalCaloriesByDate(userId, date)
        val consumedCaloriesResult = mealIntakeRepository.getTotalConsumedCalories(userId, date)

        return when {
            plannedCaloriesResult.isError -> Result.Error(message = plannedCaloriesResult.errorMessage())
            consumedCaloriesResult.isError -> Result.Error(message = consumedCaloriesResult.errorMessage())
            else -> {
                val plannedCalories = plannedCaloriesResult.getOrThrow() ?: 0.0
                val consumedCalories = consumedCaloriesResult.getOrThrow() ?: 0.0

                val nutritionInfo = NutritionInfo(
                    date = date,
                    targetCalories = targetCalories,
                    plannedCalories = plannedCalories,
                    consumedCalories = consumedCalories
                )
                Result.Success(nutritionInfo)
            }
        }
    }
}