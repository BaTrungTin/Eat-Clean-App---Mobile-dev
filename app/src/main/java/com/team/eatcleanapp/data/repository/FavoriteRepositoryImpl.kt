package com.team.eatcleanapp.data.repository

import com.team.eatcleanapp.data.local.dao.FavoriteDao
import com.team.eatcleanapp.data.local.entities.FavoriteEntity
import com.team.eatcleanapp.domain.model.Ingredient
import com.team.eatcleanapp.domain.model.Meal
import com.team.eatcleanapp.domain.repository.FavoriteRepository
import com.team.eatcleanapp.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteDao: FavoriteDao
) : FavoriteRepository {
    
    override fun getFavorites(userId: String): Flow<Result<List<Meal>>> {
        return favoriteDao.getAllFavoritesByUserId(userId)
            .map { entities ->
                val meals = entities.map { it.toDomainModel() }
                Result.Success(meals) as Result<List<Meal>>
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
            val entity = FavoriteEntity(
                userId = userId,
                mealId = mealId,
                mealName = mealName,
                calories = calories,
                image = image,
                category = category,
                createdAt = System.currentTimeMillis()
            )
            favoriteDao.addFavorite(entity)
            Result.Success(0L)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override suspend fun removeFavorite(userId: String, mealId: String): Result<Unit> {
        return try {
            favoriteDao.removeFavorite(userId, mealId)
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
        // Tạo một thành phần giả (dummy ingredient) để lưu giá trị calo đã lưu
        // Vì Meal tính toán totalCalories từ danh sách ingredients
        val dummyIngredient = Ingredient(
            name = "Stored Calories",
            quantity = 100.0, // 100g
            caloriesPer100 = this.calories // Calo trên 100g = tổng calo đã lưu
        )

        return Meal(
            id = mealId,
            name = mealName,
            image = image,
            ingredients = listOf(dummyIngredient), // Truyền dummy list vào
            instructions = emptyList()
        )
    }
}
