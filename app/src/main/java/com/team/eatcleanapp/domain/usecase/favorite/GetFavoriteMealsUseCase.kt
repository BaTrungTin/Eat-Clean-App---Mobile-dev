package com.team.eatcleanapp.domain.usecase.favorite

import com.team.eatcleanapp.domain.model.Favorite
import com.team.eatcleanapp.domain.repository.FavoriteRepository
import com.team.eatcleanapp.util.Result

class GetFavoriteMealsUseCase(
    private val repository: FavoriteRepository
) {
    suspend operator fun invoke(userId: String): Result<List<Favorite>> {
        if (userId.isBlank()) {
            return Result.Error(IllegalArgumentException("User ID không được để trống"))
        }

        return repository.getFavoritesByUserId(userId)
    }
}