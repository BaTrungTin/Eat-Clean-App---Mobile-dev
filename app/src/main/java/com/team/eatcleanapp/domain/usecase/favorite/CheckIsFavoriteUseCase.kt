package com.team.eatcleanapp.domain.usecase.favorite

import com.team.eatcleanapp.domain.repository.FavoriteRepository
import com.team.eatcleanapp.util.Result
import javax.inject.Inject

class CheckIsFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    suspend operator fun invoke(userId: String, mealId: String): Result<Boolean> {
        return favoriteRepository.isFavorite(userId, mealId)
    }
}