package com.team.eatcleanapp.domain.repository

import com.team.eatcleanapp.domain.model.Meal
import com.team.eatcleanapp.util.Result

interface MealRepository {

    //Lấy tất cả món ăn
    suspend fun getAllMeals(): Result<List<Meal>>

    //Tìm kiếm món theo tên
    suspend fun searchMeals(query: String): Result<List<Meal>>

    // Tìm kiếm món theo kcal
    suspend fun searchMealsByCalories(minCalories: Double, maxCalories: Double): Result<List<Meal>>

    // Lấy chi tiết 1 món ăn theo id
    suspend fun getMealById(mealId: String): Result<Meal?>

    // Lấy meals theo list ids
    suspend fun getMealsByIds(mealIds: List<String>): Result<List<Meal>>

    // Refresh meals từ Firestore
    suspend fun pullLatestMeals(): Result<Unit>

    // Kiểm tra meal có tồn tại không
    suspend fun mealExists(mealId: String): Result<Boolean>
}