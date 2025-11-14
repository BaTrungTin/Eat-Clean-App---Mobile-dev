package com.team.eatcleanapp.data.repository

import com.team.eatcleanapp.data.local.dao.FavoriteDao
import com.team.eatcleanapp.data.local.entities.FavoriteEntity
import com.team.eatcleanapp.domain.model.Meal
import com.team.eatcleanapp.domain.model.MealCategory
import com.team.eatcleanapp.domain.repository.FavoriteRepository
import com.team.eatcleanapp.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class FavoriteRepositoryImpl(
    private val favoriteDao: FavoriteDao
) : FavoriteRepository {
    
    override fun getFavorites(userId: String): Flow<Result<List<Meal>>> {
        return favoriteDao.getAllFavoritesByUserId(userId)
            .map { entities ->
                Result.Success(entities.map { it.toDomainModel() })
            }
            .catch { e ->
                emit(Result.Error(e))
            }
    }
    
    override suspend fun isFavorite(userId: String, mealId: String): Result<Boolean> {
        return try {
            val isFav = favoriteDao.isFavorite(userId, mealId)
            Result.Success(isFav)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override suspend fun addFavorite(
        userId: String,
        mealId: String,
        mealName: String,
        calories: Double,
        image: String,
        category: String
    ): Result<Long> {
        return try {
            // Check if already favorite
            val existing = favoriteDao.getFavoriteByMealId(userId, mealId)
            if (existing != null) {
                return Result.Success(existing.id)
            }
            
            val entity = FavoriteEntity(
                userId = userId,
                mealId = mealId,
                mealName = mealName,
                calories = calories,
                image = image,
                category = category,
                createdAt = System.currentTimeMillis()
            )
            val id = favoriteDao.insertFavorite(entity)
            Result.Success(id)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override suspend fun removeFavorite(userId: String, mealId: String): Result<Unit> {
        return try {
            favoriteDao.deleteFavoriteByMealId(userId, mealId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override suspend fun getFavoriteCount(userId: String): Result<Int> {
        return try {
            val count = favoriteDao.getFavoriteCount(userId)
            Result.Success(count)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    private fun FavoriteEntity.toDomainModel(): Meal {
        val category = try {
            MealCategory.valueOf(category)
        } catch (e: Exception) {
            MealCategory.BREAKFAST
        }
        
        return Meal(
            id = mealId,
            name = mealName,
            calories = calories,
            image = image,
            ingredients = emptyList(), // Favorites might not have full details
            instructions = emptyList(),
            category = category,
            isFavorite = true
        )
    }
}

