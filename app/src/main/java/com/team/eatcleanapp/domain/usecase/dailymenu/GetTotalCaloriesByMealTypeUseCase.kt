package com.team.eatcleanapp.domain.usecase.dailymenu

import com.team.eatcleanapp.domain.model.enums.MealCategory
import com.team.eatcleanapp.domain.repository.DailyMenuRepository
import com.team.eatcleanapp.util.Result
import java.util.Date
import javax.inject.Inject

class GetTotalCaloriesByMealTypeUseCase @Inject constructor(
    private val dailyMenuRepository: DailyMenuRepository
) {
    suspend operator fun invoke(userId: String, date: Date, mealType: MealCategory): Result<Double> {
        val result = dailyMenuRepository.getTotalCaloriesByType(userId, date, mealType)
        return when (result) {
            is Result.Success -> Result.Success(result.data ?: 0.0)
            is Result.Error -> Result.Error(result.exception, result.message)
            else -> Result.Error(message = "Không xác định")
        }
    }
}