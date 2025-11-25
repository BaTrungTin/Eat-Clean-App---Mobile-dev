package com.team.eatcleanapp.domain.usecase.favorite

import com.team.eatcleanapp.domain.repository.FavoriteRepository
import com.team.eatcleanapp.domain.repository.MealOverrideRepository
import com.team.eatcleanapp.domain.repository.MealRepository
import com.team.eatcleanapp.util.Result
import java.util.Date
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val mealRepository: MealRepository,
    private val mealOverrideRepository: MealOverrideRepository
) {
    suspend operator fun invoke(userId: String, mealId: String): Result<Boolean> {
        val isFavoriteResult = favoriteRepository.isFavorite(userId, mealId)
        if (isFavoriteResult.isError) return isFavoriteResult

        return if (isFavoriteResult.getOrThrow()) {
            val removeResult = favoriteRepository.removeFavorite(userId, mealId)
            if (removeResult.isSuccess) {
                mealOverrideRepository.removeMealOverride(userId, mealId)
                Result.Success(false)
            } else {
                Result.Error(message = removeResult.errorMessage())
            }
        } else {
            val mealResult = mealRepository.getMealDetail(mealId)
            if (mealResult.isError || mealResult.getOrNull() == null) {
                return Result.Error(message = "Món ăn không tồn tại")
            }

            val meal = mealResult.getOrThrow()!!
            val favorite = com.team.eatcleanapp.domain.model.meal.Favorite(
                userId = userId,
                mealId = mealId,
                mealName = meal.name,
                calories = meal.calories,
                image = meal.image,
                createdAt = Date()
            )

            val addResult = favoriteRepository.addFavorite(favorite)
            if (addResult.isSuccess) {
                Result.Success(true)
            } else {
                Result.Error(message = addResult.errorMessage())
            }
        }
    }
}