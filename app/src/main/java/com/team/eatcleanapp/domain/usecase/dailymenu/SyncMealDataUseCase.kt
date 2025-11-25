package com.team.eatcleanapp.domain.usecase.dailymenu

import com.team.eatcleanapp.domain.repository.DailyMenuRepository
import com.team.eatcleanapp.util.Result
import javax.inject.Inject

class SyncMealDataUseCase @Inject constructor(
    private val dailyMenuRepository: DailyMenuRepository
) {
    suspend operator fun invoke(mealId: String, newMealName: String, newCalories: Double): Result<Unit> {
        if (newMealName.isBlank()) return Result.Error(message = "Tên món ăn không được để trống")
        if (newCalories < 0) return Result.Error(message = "Calories không được âm")

        return dailyMenuRepository.updateDailyMenuMealInfo(mealId, newMealName, newCalories)
    }
}