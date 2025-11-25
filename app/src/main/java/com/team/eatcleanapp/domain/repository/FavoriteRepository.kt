package com.team.eatcleanapp.domain.repository

import com.team.eatcleanapp.domain.model.meal.Favorite
import com.team.eatcleanapp.domain.model.meal.Meal
import com.team.eatcleanapp.util.Result
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    // lay tat ca mon an yeu thich
    fun getFavorites(userId: String): Flow<Result<List<Favorite>>>

    // kiem tra mon an co trong favorite khong
    suspend fun isFavorite(userId: String, mealId: String): Result<Boolean>

    // them mon an vao favorite
    suspend fun addFavorite(favorite: Favorite): Result<Unit>

    // xoa mon an khoi favorite
    suspend fun removeFavorite(userId: String, mealId: String): Result<Unit>

    // xoa tat ca favorite cua user
    suspend fun clearFavorites(userId: String): Result<Unit>

    // dem so luong favorite
    suspend fun getFavoriteCount(userId: String): Result<Int>

    // cap nhat thong tin mon an trong favorite khi meal data thay doi
    suspend fun updateFavoriteMealInfo(
        mealId: String,
        newMealName: String,
        newCalories: Double
    ): Result<Unit>

    // xoa mon an khoi favorite khi meal bi xoa khoi firestore
    suspend fun removeFavoriteByMealId(mealId: String): Result<Unit>
}

