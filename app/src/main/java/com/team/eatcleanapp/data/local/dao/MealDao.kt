package com.team.eatcleanapp.data.local.dao

import androidx.room.*
import com.team.eatcleanapp.data.local.entities.MealEntity

@Dao
interface MealDao {
    // thêm một món vào meals
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: MealEntity)

    // thêm nhiều món vào meals
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeals(meals: List<MealEntity>)

    // cập nhật thông tin của 1 món
    @Update
    suspend fun update(meal: MealEntity)

    // xóa một món
    @Delete
    suspend fun deleteMeal(meal: MealEntity)

    // xóa 1 món theo mealid
    @Query("DELETE FROM meals WHERE id = :mealId")
    suspend fun deleteMealById(mealId: String)

    // xóa tất cả món trong meals
    @Query("DELETE FROM meals")
    suspend fun deleteAllMeals()

    // lấy 1 món theo mealid
    @Query("SELECT * FROM meals WHERE id = :mealId")
    suspend fun getMealById(mealId: String): MealEntity?

    // lấy tất cả món
    @Query("SELECT * FROM meals ORDER BY name COLLATE NOCASE ASC")
    suspend fun getAllMeals(): List<MealEntity>

    // tìm kiếm món theo tên
    @Query("SELECT * FROM meals WHERE name LIKE '%' || :query || '%' OR calories LIKE '%' || :query || '%' ORDER BY name COLLATE NOCASE ASC")
    suspend fun searchMeals(query: String): List<MealEntity>

    // tìm kiếm món theo kcal
    @Query("SELECT * FROM meals WHERE calories BETWEEN :minCalories AND :maxCalories")
    suspend fun searchMealsByCalories(minCalories: Double, maxCalories: Double): List<MealEntity>

    // lấy nhiều món theo ids
    @Query("SELECT * FROM meals WHERE id IN (:mealIds)")
    suspend fun getMealsByIds(mealIds: List<String>): List<MealEntity>

    // kiểm tra xem có món đó trong meals không
    @Query("SELECT COUNT(*) FROM meals WHERE id = :mealId")
    suspend fun mealExists(mealId: String): Int
}
