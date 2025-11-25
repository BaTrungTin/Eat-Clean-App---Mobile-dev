package com.team.eatcleanapp.domain.usecase.favorite

import com.team.eatcleanapp.domain.model.meal.Meal
import com.team.eatcleanapp.domain.repository.MealOverrideRepository
import com.team.eatcleanapp.domain.repository.MealRepository
import com.team.eatcleanapp.util.Result
import javax.inject.Inject

class GetMealWithOverrideUseCase @Inject constructor(
    private val mealRepository: MealRepository,
    private val mealOverrideRepository: MealOverrideRepository
) {
    suspend operator fun invoke(userId: String, mealId: String): Result<Meal> {
        val mealResult = mealRepository.getMealDetail(mealId)
        if (mealResult.isError || mealResult.getOrNull() == null) {
            return Result.Error(message = "Món ăn không tồn tại")
        }

        val originalMeal = mealResult.getOrThrow()!!
        return mealOverrideRepository.getMealWithOverride(userId, originalMeal)
    }
}