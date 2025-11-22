package com.team.eatcleanapp.data.remote.firebase

import com.google.firebase.database.FirebaseDatabase
import com.team.eatcleanapp.domain.model.Meal
import com.team.eatcleanapp.util.Result
import kotlinx.coroutines.tasks.await

class RealtimeDatabaseService {

    private val database = FirebaseDatabase.getInstance()
    private val mealsRef = database.getReference("meals")

    suspend fun getAllMeals(): Result<List<Meal>> {
        return try {
            val snapshot = mealsRef.get().await()
            val meals = snapshot.children.mapNotNull { child ->
                child.getValue(Meal::class.java)
            }
            Result.Success(meals)
        } catch (e: Exception) {
            Result.Error(Exception("Lỗi lấy danh sách món ăn từ Realtime DB: ${e.message}"))
        }
    }

    suspend fun getMealById(mealId: String): Result<Meal?> {
        return try {
            val snapshot = mealsRef.child(mealId).get().await()
            val meal = snapshot.getValue(Meal::class.java)
            Result.Success(meal)
        } catch (e: Exception) {
            Result.Error(Exception("Lỗi lấy chi tiết món ăn: ${e.message}"))
        }
    }

    suspend fun searchMealsByName(query: String): Result<List<Meal>> {
        return try {
            val snapshot = mealsRef.get().await()
            val meals = snapshot.children.mapNotNull { child ->
                child.getValue(Meal::class.java)
            }.filter { meal ->
                meal.name.contains(query, ignoreCase = true)
            }
            Result.Success(meals)
        } catch (e: Exception) {
            Result.Error(Exception("Lỗi tìm kiếm món ăn: ${e.message}"))
        }
    }

    suspend fun getMealsByCaloriesRange(minCalories: Double, maxCalories: Double): Result<List<Meal>> {
        return try {
            val snapshot = mealsRef.get().await()
            val meals = snapshot.children.mapNotNull { child ->
                child.getValue(Meal::class.java)
            }.filter { meal ->
                meal.calories in minCalories..maxCalories
            }.sortedBy { it.calories }
            Result.Success(meals)
        } catch (e: Exception) {
            Result.Error(Exception("Lỗi tìm kiếm món ăn theo kcal: ${e.message}"))
        }
    }

    suspend fun getMealsByIds(mealIds: List<String>): Result<List<Meal>> {
        return try {
            val meals = mealIds.mapNotNull { mealId ->
                getMealById(mealId).getOrNull()
            }
            Result.Success(meals)
        } catch (e: Exception) {
            Result.Error(Exception("Lỗi lấy món ăn theo danh sách ID: ${e.message}"))
        }
    }

    suspend fun mealExists(mealId: String): Result<Boolean> {
        return try {
            val snapshot = mealsRef.child(mealId).get().await()
            Result.Success(snapshot.exists())
        } catch (e: Exception) {
            Result.Error(Exception("Lỗi kiểm tra món ăn: ${e.message}"))
        }
    }
}