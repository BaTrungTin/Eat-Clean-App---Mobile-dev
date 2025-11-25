package com.team.eatcleanapp.domain.usecase.favorite

import com.team.eatcleanapp.domain.repository.FavoriteRepository
import com.team.eatcleanapp.domain.repository.MealRepository
import com.team.eatcleanapp.util.Result
import javax.inject.Inject

class AddToFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val mealRepository: MealRepository
) {
    suspend operator fun invoke(
        userId: String,
        mealId: String
    ): Result<Long> {
        if (userId.isBlank() || mealId.isBlank()) {
            return Result.Error(IllegalArgumentException("UserId và MealId không được để trống"))
        }

        return try {
            val meal = mealRepository.getMealDetail(mealId)
                ?: return Result.Error(IllegalArgumentException("Không tìm thấy món ăn với id: $mealId"))

            favoriteRepository.addFavorite(
                userId = userId,
                mealId = mealId,
                mealName = meal.name,
                calories = meal.totalCalories,
                image = meal.image ?: "",
                category = "GENERAL"
            )
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
