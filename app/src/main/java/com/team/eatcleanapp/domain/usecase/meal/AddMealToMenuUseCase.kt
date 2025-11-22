package com.team.eatcleanapp.domain.usecase.favorite

import com.team.eatcleanapp.domain.repository.FavoriteRepository
import com.team.eatcleanapp.domain.repository.MealRepository
import com.team.eatcleanapp.util.Result

class AddToFavoriteUseCase(
    private val favoriteRepository: FavoriteRepository,
    private val mealRepository: MealRepository
) {
    suspend operator fun invoke(
        userId: String,
        mealId: String
    ): Result<Unit> {
        if (userId.isBlank() || mealId.isBlank()) {
            return Result.Error(IllegalArgumentException("UserId và MealId không được để trống"))
        }

        val mealResult = mealRepository.getMealById(mealId)
        return when (mealResult) {
            is Result.Success -> {
                val meal = mealResult.data
                if (meal != null) {
                    favoriteRepository.addToFavorite(userId, meal)
                } else {
                    Result.Error(IllegalArgumentException("Không tìm thấy món ăn với id: $mealId"))
                }
            }
            is Result.Error -> mealResult
            is Result.Loading -> Result.Error(IllegalStateException("Đang tải thông tin món ăn"))
        }
    }
}