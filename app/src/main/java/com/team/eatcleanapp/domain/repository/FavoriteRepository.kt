package com.team.eatcleanapp.domain.repository

import com.team.eatcleanapp.domain.model.Favorite
import com.team.eatcleanapp.domain.model.Meal
import com.team.eatcleanapp.util.Result

interface FavoriteRepository {
    // Lấy tất cả favorites
    suspend fun getFavoritesByUserId(userId: String): Result<List<Favorite>>

    // Kiểm tra có favorite không
    suspend fun isFavorite(userId: String, mealId: String): Result<Boolean>

    // Thêm vào favorite
    suspend fun addToFavorite(userId: String, meal: Meal): Result<Unit>

    // Xóa khỏi favorite
    suspend fun removeFromFavorite(userId: String, mealId: String): Result<Unit>

    // Cập nhật customized meal
    suspend fun updateCustomizedMeal(userId: String, mealId: String, updatedMeal: Meal): Result<Unit>

    // Lấy meal detail (ưu tiên từ favorite nếu customized)
    suspend fun getMealDetail(userId: String, mealId: String): Result<Meal>

    // Đếm số favorites
    suspend fun getFavoriteCount(userId: String): Result<Int>

    suspend fun deleteAllFavoritesByUserId(userId: String): Result<Unit>
}