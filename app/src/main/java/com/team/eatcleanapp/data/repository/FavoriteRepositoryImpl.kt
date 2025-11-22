package com.team.eatcleanapp.data.repository

import com.team.eatcleanapp.data.local.dao.FavoriteDao
import com.team.eatcleanapp.data.local.entities.FavoriteEntity
import com.team.eatcleanapp.domain.model.Favorite
import com.team.eatcleanapp.domain.model.Ingredient
import com.team.eatcleanapp.domain.model.Meal
import com.team.eatcleanapp.domain.repository.FavoriteRepository
import com.team.eatcleanapp.domain.repository.MealRepository
import com.team.eatcleanapp.util.Result

class FavoriteRepositoryImpl(
    private val favoriteDao: FavoriteDao,
    private val mealRepository: MealRepository
) : FavoriteRepository {

    override suspend fun getFavoritesByUserId(userId: String): Result<List<Favorite>> {
        return try {
            val favorites = favoriteDao.getAllFavoritesByUserId(userId)
            val meals = favorites.map { it.toDomain() }
            Result.Success(meals)
        } catch (e: Exception) {
            Result.Error(Exception("Lỗi lấy danh sách yêu thích: ${e.message}"))
        }
    }

    override suspend fun isFavorite(userId: String, mealId: String): Result<Boolean> {
        return try {
            val isFavorite = favoriteDao.isFavorite(userId, mealId)
            Result.Success(isFavorite)
        } catch (e: Exception) {
            Result.Error(Exception("Lỗi kiểm tra yêu thích: ${e.message}"))
        }
    }

    override suspend fun addToFavorite(userId: String, meal: Meal): Result<Unit> {
        return try {
            val entity = FavoriteEntity(
                userId = userId,
                mealId = meal.id,
                mealName = meal.name,
                calories = meal.calories,
                image = meal.image ?: "",
                ingredients = ingredientsToString(meal.ingredients),
                instructions = instructionsToString(meal.instructions),
                isCustomized = false
            )
            favoriteDao.insertFavorite(entity)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(Exception("Lỗi thêm vào yêu thích: ${e.message}"))
        }
    }

    override suspend fun removeFromFavorite(userId: String, mealId: String): Result<Unit> {
        return try {
            favoriteDao.deleteFavorite(userId, mealId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(Exception("Lỗi xóa khỏi yêu thích: ${e.message}"))
        }
    }

    override suspend fun updateCustomizedMeal(userId: String, mealId: String, updatedMeal: Meal): Result<Unit> {
        return try {
            val updatedEntity = FavoriteEntity(
                userId = userId,
                mealId = mealId,
                mealName = updatedMeal.name,
                calories = updatedMeal.calories,
                image = updatedMeal.image ?: "",
                ingredients = ingredientsToString(updatedMeal.ingredients),
                instructions = instructionsToString(updatedMeal.instructions),
                isCustomized = true
                // BỎ các trường original
            )
            favoriteDao.update(updatedEntity)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(Exception("Lỗi cập nhật món ăn tùy chỉnh: ${e.message}"))
        }
    }

    override suspend fun getMealDetail(userId: String, mealId: String): Result<Meal> {
        return try {
            val favorite = favoriteDao.getFavoriteByMealId(userId, mealId)
            if (favorite != null) {
                val meal = Meal(
                    id = favorite.mealId,
                    name = favorite.mealName,
                    calories = favorite.calories,
                    image = favorite.image,
                    ingredients = stringToIngredients(favorite.ingredients),
                    instructions = stringToInstructions(favorite.instructions)
                )
                Result.Success(meal)
            } else {
                Result.Error(Exception("Không tìm thấy chi tiết món ăn"))
            }
        } catch (e: Exception) {
            Result.Error(Exception("Lỗi lấy chi tiết món ăn: ${e.message}"))
        }
    }

    override suspend fun getFavoriteCount(userId: String): Result<Int> {
        return try {
            val count = favoriteDao.getCountByUserId(userId)
            Result.Success(count)
        } catch (e: Exception) {
            Result.Error(Exception("Lỗi đếm số yêu thích: ${e.message}"))
        }
    }

    // FavoriteEntity → Favorite (Domain)
    private fun FavoriteEntity.toDomain(): Favorite = Favorite(
        userId = userId,
        mealId = mealId,
        mealName = mealName,
        calories = calories,
        image = image,
        ingredients = stringToIngredients(ingredients),
        instructions = stringToInstructions(instructions),
        isCustomized = isCustomized
        // BỎ createdAt
    )

    // Helper functions
    private fun ingredientsToString(ingredients: List<Ingredient>): String {
        return ingredients.joinToString("\n") { ingredient ->
            "${ingredient.name}|${ingredient.amount}"
        }
    }

    private fun instructionsToString(instructions: List<String>): String {
        return instructions.mapIndexed { index, instruction ->
            "B${index + 1}. $instruction"
        }.joinToString("\n")
    }

    private fun stringToIngredients(ingredientsString: String): List<Ingredient> {
        return if (ingredientsString.isBlank()) {
            emptyList()
        } else {
            ingredientsString.split("\n").mapNotNull { line ->
                val parts = line.split("|")
                if (parts.size == 2) {
                    Ingredient(
                        name = parts[0].trim(),
                        amount = parts[1].trim()
                    )
                } else {
                    null
                }
            }
        }
    }

    private fun stringToInstructions(instructionsString: String): List<String> {
        return if (instructionsString.isBlank()) {
            emptyList()
        } else {
            instructionsString.split("\n").map { line ->
                line.replace(Regex("^B\\d+\\.\\s*"), "").trim()
            }.filter { it.isNotBlank() }
        }
    }

    override suspend fun deleteAllFavoritesByUserId(userId: String): Result<Unit> {
        return try {
            favoriteDao.deleteAllFavoritesByUserId(userId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(Exception("Lỗi xóa danh sách yêu thích: ${e.message}"))
        }
    }
}