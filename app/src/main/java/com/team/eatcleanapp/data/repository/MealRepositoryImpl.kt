package com.team.eatcleanapp.data.repository

import com.team.eatcleanapp.data.local.dao.MealDao
import com.team.eatcleanapp.data.local.entities.MealEntity
import com.team.eatcleanapp.data.remote.firebase.RealtimeDatabaseService
import com.team.eatcleanapp.domain.model.Meal
import com.team.eatcleanapp.domain.model.Ingredient
import com.team.eatcleanapp.domain.repository.MealRepository
import com.team.eatcleanapp.util.Result

class MealRepositoryImpl(
    private val mealDao: MealDao,
    private val realtimeDatabaseService: RealtimeDatabaseService
) : MealRepository {

    override suspend fun getAllMeals(): Result<List<Meal>> {
        return try {
            // Ưu tiên lấy từ local trước
            val localMeals = mealDao.getAllMeals().map { it.toDomain() }
            if (localMeals.isNotEmpty()) {
                Result.Success(localMeals)
            } else {
                // Nếu local empty, lấy từ Realtime DB
                pullLatestMeals()
                val updatedMeals = mealDao.getAllMeals().map { it.toDomain() }
                Result.Success(updatedMeals)
            }
        } catch (e: Exception) {
            Result.Error(Exception("Lỗi lấy danh sách món ăn: ${e.message}"))
        }
    }

    override suspend fun searchMeals(query: String): Result<List<Meal>> {
        return try {
            // Tìm trong local trước
            val localResults = mealDao.searchMeals(query).map { it.toDomain() }
            if (localResults.isNotEmpty()) {
                Result.Success(localResults)
            } else {
                // Nếu không tìm thấy trong local, tìm trong Realtime DB
                val remoteResults = realtimeDatabaseService.searchMealsByName(query)
                if (remoteResults is Result.Success) {
                    // Lưu kết quả tìm được vào local
                    val entities = remoteResults.data.map { it.toEntity() }
                    mealDao.insertMeals(entities)
                    Result.Success(remoteResults.data)
                } else {
                    Result.Success(emptyList())
                }
            }
        } catch (e: Exception) {
            Result.Error(Exception("Lỗi tìm kiếm món ăn: ${e.message}"))
        }
    }

    override suspend fun searchMealsByCalories(minCalories: Double, maxCalories: Double): Result<List<Meal>> {
        return try {
            // Tìm trong local trước
            val localResults = mealDao.searchMealsByCalories(minCalories, maxCalories).map { it.toDomain() }
            if (localResults.isNotEmpty()) {
                Result.Success(localResults)
            } else {
                // Nếu không tìm thấy trong local, tìm trong Realtime DB
                val remoteResults = realtimeDatabaseService.getMealsByCaloriesRange(minCalories, maxCalories)
                if (remoteResults is Result.Success) {
                    // Lưu kết quả tìm được vào local
                    val entities = remoteResults.data.map { it.toEntity() }
                    mealDao.insertMeals(entities)
                    Result.Success(remoteResults.data)
                } else {
                    Result.Success(emptyList())
                }
            }
        } catch (e: Exception) {
            Result.Error(Exception("Lỗi tìm kiếm món ăn theo kcal: ${e.message}"))
        }
    }

    override suspend fun getMealById(mealId: String): Result<Meal?> {
        return try {
            // Ưu tiên lấy từ local
            val localMeal = mealDao.getMealById(mealId)?.toDomain()
            if (localMeal != null) {
                Result.Success(localMeal)
            } else {
                // Nếu không có trong local, lấy từ Realtime DB
                val remoteResult = realtimeDatabaseService.getMealById(mealId)
                if (remoteResult is Result.Success) {
                    val remoteMeal = remoteResult.data
                    remoteMeal?.let { meal ->
                        // Lưu vào local để lần sau
                        mealDao.insertMeal(meal.toEntity())
                    }
                    Result.Success(remoteMeal)
                } else {
                    Result.Success(null)
                }
            }
        } catch (e: Exception) {
            Result.Error(Exception("Lỗi lấy chi tiết món ăn: ${e.message}"))
        }
    }

    override suspend fun getMealsByIds(mealIds: List<String>): Result<List<Meal>> {
        return try {
            // Lấy từ local trước
            val localMeals = mealDao.getMealsByIds(mealIds).map { it.toDomain() }

            // Kiểm tra xem có thiếu meal nào không
            val foundIds = localMeals.map { it.id }.toSet()
            val missingIds = mealIds - foundIds

            if (missingIds.isEmpty()) {
                Result.Success(localMeals)
            } else {
                // Lấy các meal bị thiếu từ Realtime DB
                val missingMealsResult = realtimeDatabaseService.getMealsByIds(missingIds.toList())
                if (missingMealsResult is Result.Success) {
                    val missingMeals = missingMealsResult.data
                    // Lưu các meal mới vào local
                    val entities = missingMeals.map { it.toEntity() }
                    mealDao.insertMeals(entities)
                    // Kết hợp kết quả
                    val allMeals = localMeals + missingMeals
                    Result.Success(allMeals)
                } else {
                    Result.Success(localMeals)
                }
            }
        } catch (e: Exception) {
            Result.Error(Exception("Lỗi lấy món ăn theo danh sách ID: ${e.message}"))
        }
    }

    override suspend fun pullLatestMeals(): Result<Unit> {
        return try {
            // Lấy data mới nhất từ Realtime DB
            val remoteResult = realtimeDatabaseService.getAllMeals()
            if (remoteResult is Result.Success) {
                val mealsFromRemote = remoteResult.data
                val entities = mealsFromRemote.map { it.toEntity() }
                // Cập nhật local database
                mealDao.deleteAllMeals()
                mealDao.insertMeals(entities)
                Result.Success(Unit)
            } else {
                Result.Error(Exception("Không thể đồng bộ dữ liệu từ server"))
            }
        } catch (e: Exception) {
            Result.Error(Exception("Lỗi đồng bộ món ăn từ Realtime DB: ${e.message}"))
        }
    }

    override suspend fun mealExists(mealId: String): Result<Boolean> {
        return try {
            // Kiểm tra trong local trước
            val localExists = mealDao.mealExists(mealId) > 0
            if (localExists) {
                Result.Success(true)
            } else {
                // Nếu không có trong local, kiểm tra trong Realtime DB
                val remoteResult = realtimeDatabaseService.mealExists(mealId)
                if (remoteResult is Result.Success) {
                    Result.Success(remoteResult.data)
                } else {
                    Result.Success(false)
                }
            }
        } catch (e: Exception) {
            Result.Error(Exception("Lỗi kiểm tra món ăn: ${e.message}"))
        }
    }

    // MealEntity → Meal (Domain)
    private fun MealEntity.toDomain(): Meal = Meal(
        id = id,
        name = name,
        calories = calories,
        image = image,
        ingredients = stringToIngredients(ingredients),
        instructions = parseStringToList(instructions)
    )

    // Meal (Domain) → MealEntity
    private fun Meal.toEntity(): MealEntity = MealEntity(
        id = id,
        name = name,
        calories = calories,
        image = image,
        ingredients = ingredientsToString(ingredients),
        instructions = listToString(instructions)
    )

    // Helper functions cho Ingredients
    private fun ingredientsToString(ingredients: List<Ingredient>): String {
        return ingredients.joinToString("|") { ingredient ->
            "${ingredient.name}|${ingredient.amount}"
        }
    }

    private fun stringToIngredients(ingredientsString: String): List<Ingredient> {
        return if (ingredientsString.isBlank()) {
            emptyList()
        } else {
            ingredientsString.split("|").mapNotNull { line ->
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

    // Helper functions cho Instructions (List<String>)
    private fun parseStringToList(string: String): List<String> {
        return if (string.isBlank()) emptyList()
        else string.split("|").map { it.trim() }.filter { it.isNotBlank() }
    }

    private fun listToString(list: List<String>): String {
        return list.joinToString("|")
    }
}