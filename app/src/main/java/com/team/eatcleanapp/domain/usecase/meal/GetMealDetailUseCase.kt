package com.team.eatcleanapp.domain.usecase.meal

import com.team.eatcleanapp.domain.model.Meal
import com.team.eatcleanapp.domain.repository.MealRepository
import com.team.eatcleanapp.util.Result

class GetMealDetailUseCase(
    private val mealRepository: MealRepository
) {
    suspend operator fun invoke(mealId: String): Result<Meal?> {
        if (mealId.isBlank()) {
            return Result.Error(IllegalArgumentException("Meal ID không được để trống"))
        }

        return mealRepository.getMealById(mealId)
    }
}