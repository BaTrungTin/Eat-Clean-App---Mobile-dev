package com.team.eatcleanapp.domain.usecase.dailymenu

import com.team.eatcleanapp.domain.model.enums.MealCategory
import com.team.eatcleanapp.domain.repository.DailyMenuRepository
import com.team.eatcleanapp.util.Result
import java.util.Date
import javax.inject.Inject

class UpdatePortionSizeUseCase @Inject constructor(
    private val dailyMenuRepository: DailyMenuRepository
) {
    suspend operator fun invoke(
        userId: String,
        date: Date,
        mealId: String,
        mealType: MealCategory,
        portionSize: Double
    ): Result<Unit> {
        if (portionSize <= 0) return Result.Error(message = "Khẩu phần phải lớn hơn 0")

        val mealResult = dailyMenuRepository.getDailyMenuItem(userId, date, mealId, mealType)
        return when {
            mealResult.isError -> Result.Error(message = mealResult.errorMessage())
            mealResult.getOrNull() == null -> Result.Error(message = "Không tìm thấy món ăn trong thực đơn")
            else -> {
                val meal = mealResult.getOrThrow()!!
                val updatedMeal = meal.copy(portionSize = portionSize)
                dailyMenuRepository.updateDailyMenu(updatedMeal)
            }
        }
    }
}