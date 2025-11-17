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
    ): Result<Long> {
        if (userId.isBlank() || mealId.isBlank()) {
            return Result.Error(IllegalArgumentException("UserId và MealId không được để trống"))
        }

        return try {
            // Lấy thông tin meal để có đầy đủ thông tin cần thiết
            val meal = mealRepository.getMealDetail(mealId)
                ?: return Result.Error(IllegalArgumentException("Không tìm thấy món ăn với id: $mealId"))

            // Thêm vào favorite với đầy đủ thông tin
            favoriteRepository.addFavorite(
                userId = userId,
                mealId = mealId,
                mealName = meal.name,
                calories = meal.calories,
                image = meal.image ?: "",
                category = "GENERAL" // Default category, có thể cải thiện sau
            )
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}

