package com.team.eatcleanapp.domain.usecase.dailymenu

import com.team.eatcleanapp.domain.model.enums.MealCategory
import com.team.eatcleanapp.domain.repository.DailyMenuRepository
import com.team.eatcleanapp.util.Result
import java.util.Date
import javax.inject.Inject

class CheckMealInMenuUseCase @Inject constructor(
    private val dailyMenuRepository: DailyMenuRepository
) {
    suspend operator fun invoke(
        userId: String,
        date: Date,
        mealId: String,
        mealType: MealCategory
    ): Result<Boolean> {
        return dailyMenuRepository.mealExistsInMenu(userId, date, mealId, mealType)
    }
}