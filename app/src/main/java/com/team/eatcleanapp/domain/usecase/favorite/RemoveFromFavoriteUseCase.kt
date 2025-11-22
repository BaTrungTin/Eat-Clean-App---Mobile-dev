package com.team.eatcleanapp.domain.usecase.favorite

import com.team.eatcleanapp.domain.repository.FavoriteRepository
import com.team.eatcleanapp.util.Result

class RemoveFromFavoriteUseCase(
    private val repository: FavoriteRepository
) {
    suspend operator fun invoke(
        userId: String,
        mealId: String
    ): Result<Unit> {
        if (userId.isBlank() || mealId.isBlank()) {
            return Result.Error(IllegalArgumentException("UserId và MealId không được để trống"))
        }

        return repository.removeFromFavorite(userId, mealId)
    }
}