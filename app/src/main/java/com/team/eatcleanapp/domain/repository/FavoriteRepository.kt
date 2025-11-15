package com.team.eatcleanapp.domain.repository

import com.team.eatcleanapp.domain.model.Meal
import com.team.eatcleanapp.util.Result
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    /**
     * Get all favorite meals for a user
     */
    fun getFavorites(userId: String): Flow<Result<List<Meal>>>
    
    /**
     * Check if a meal is favorite
     */
    suspend fun isFavorite(userId: String, mealId: String): Result<Boolean>
    
    /**
     * Add meal to favorites
     */
    suspend fun addFavorite(
        userId: String,
        mealId: String,
        mealName: String,
        calories: Double,
        image: String,
        category: String
    ): Result<Long>
    
    /**
     * Remove meal from favorites
     */
    suspend fun removeFavorite(userId: String, mealId: String): Result<Unit>
    
    /**
     * Get favorite count for a user
     */
    suspend fun getFavoriteCount(userId: String): Result<Int>
}

