package com.team.eatcleanapp.domain.usecase.favorite

import com.team.eatcleanapp.domain.model.meal.Meal
import com.team.eatcleanapp.domain.repository.FavoriteRepository
import com.team.eatcleanapp.domain.repository.MealOverrideRepository
import com.team.eatcleanapp.domain.repository.MealRepository
import com.team.eatcleanapp.util.Result
import javax.inject.Inject

class SaveMealOverrideUseCase @Inject constructor(
    private val mealOverrideRepository: MealOverrideRepository,
    private val favoriteRepository: FavoriteRepository,
    private val mealRepository: MealRepository
) {
    suspend operator fun invoke(userId: String, mealId: String, modifiedMeal: Meal): Result<Unit> {
        val isFavoriteResult = favoriteRepository.isFavorite(userId, mealId)
        if (isFavoriteResult.isError) return Result.Error(message = isFavoriteResult.errorMessage())
        if (!isFavoriteResult.getOrThrow()) {
            return Result.Error(message = "Chỉ có thể chỉnh sửa món đã thêm vào yêu thích")
        }

        val originalMealResult = mealRepository.getMealDetail(mealId)
        if (originalMealResult.isError || originalMealResult.getOrNull() == null) {
            return Result.Error(message = "Món ăn gốc không tồn tại")
        }

        val originalMeal = originalMealResult.getOrThrow()!!
        return mealOverrideRepository.saveMealOverride(userId, originalMeal, modifiedMeal)
    }
}