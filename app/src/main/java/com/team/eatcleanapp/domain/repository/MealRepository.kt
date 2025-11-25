package com.team.eatcleanapp.domain.repository

import com.team.eatcleanapp.domain.model.meal.Meal
import com.team.eatcleanapp.util.Result
import kotlinx.coroutines.flow.Flow


interface MealRepository {

    // Lấy tất cả món ăn.
    fun getAllMeals(): Flow<Result<List<Meal>>>

    // Tìm kiếm món theo tên
    fun searchMeals(query: String): Flow<Result<List<Meal>>>

    // tim mon an theo calories
    fun searchMealsByCalories(caloriesQuery: String): Flow<Result<List<Meal>>>

    // Lấy chi tiết 1 món ăn theo id
    suspend fun getMealDetail(id: String): Result<Meal?>

    // lay nhieu mon an theo danh sach IDs
    suspend fun getMealsByIds(mealIds: List<String>): Result<List<Meal>>

    // xoa mon an
    suspend fun deleteMeal(mealId: String): Result<Unit>

    // kiem tra mon an co ton tai khong
    suspend fun mealExists(mealId: String): Result<Boolean>

    // dong bo du lieu khi meal data thay doi tren firestore
    suspend fun syncMealsFromRemote(): Result<Unit>
}