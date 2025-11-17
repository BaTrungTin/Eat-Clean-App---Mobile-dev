package com.team.eatcleanapp.domain.usecase.favorite

import com.team.eatcleanapp.domain.model.Meal
import com.team.eatcleanapp.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class GetFavoriteMealsUseCase(
    private val repository: FavoriteRepository
) {
    suspend operator fun invoke(userId: String): List<Meal> {
        if (userId.isBlank()) {
            return emptyList()
        }

        return try {
            val result = repository.getFavorites(userId).first()
            when (result) {
                is com.team.eatcleanapp.util.Result.Success -> result.data
                else -> emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}

