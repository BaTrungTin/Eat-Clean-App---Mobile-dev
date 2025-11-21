package com.team.eatcleanapp.domain.usecase.meal

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
    ): Result<Unit> {
        if (userId.isBlank() || mealId.isBlank()) {
            return Result.Error(IllegalArgumentException("UserId và MealId không được để trống"))
        }

        return try {
            val meal = mealRepository.getMealDetail(mealId)
            
            if (meal != null) {
                val addResult = favoriteRepository.addFavorite(
                    userId = userId,
                    mealId = meal.id,
                    mealName = meal.name,
                    calories = meal.totalCalories,
                    image = meal.image ?: "",
                    category = "Uncategorized" // Default category as Meal model lacks it
                )
                
                when (addResult) {
                    is Result.Success -> Result.Success(Unit)
                    is Result.Error -> Result.Error(addResult.exception)
                    is Result.Loading -> Result.Loading
                }
            } else {
                Result.Error(IllegalArgumentException("Không tìm thấy món ăn với id: $mealId"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
