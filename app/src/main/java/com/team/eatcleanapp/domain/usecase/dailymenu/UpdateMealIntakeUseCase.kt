package com.team.eatcleanapp.domain.usecase.dailymenu

import com.team.eatcleanapp.domain.model.dailymenu.DailyMenuItem
import com.team.eatcleanapp.domain.repository.DailyMenuRepository
import com.team.eatcleanapp.util.Result
import javax.inject.Inject

class UpdateMealIntakeUseCase @Inject constructor(
    private val dailyMenuRepository: DailyMenuRepository
) {
    suspend operator fun invoke(mealItem: DailyMenuItem): Result<Unit> {
        if (mealItem.portionSize <= 0) return Result.Error(message = "Khẩu phần phải lớn hơn 0")
        if (mealItem.calories < 0) return Result.Error(message = "Calories không được âm")

        return dailyMenuRepository.updateDailyMenu(mealItem)
    }
}