package com.team.eatcleanapp.domain.usecase.favorite

import com.team.eatcleanapp.domain.model.meal.Favorite
import com.team.eatcleanapp.domain.repository.FavoriteRepository
import com.team.eatcleanapp.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetFavoriteMealsUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    operator fun invoke(userId: String): Flow<Result<List<Favorite>>> {
        return favoriteRepository.getFavorites(userId).map { result ->
            when (result) {
                is Result.Success -> {
                    val sortedFavorites = result.data.sortedByDescending { it.createdAt }
                    Result.Success(sortedFavorites)
                }
                else -> result
            }
        }
    }
}