package com.team.eatcleanapp.data.local.dao

import androidx.room.*
import com.team.eatcleanapp.data.local.entities.MealEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: MealEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeals(meals: List<MealEntity>)

    @Query("SELECT * FROM meals WHERE id = :mealId")
    fun getMealByIdFlow(mealId: String): Flow<MealEntity?>

    @Query("SELECT * FROM meals WHERE id = :mealId")
    suspend fun getMealById(mealId: String): MealEntity?

    @Query("SELECT * FROM meals ORDER BY name COLLATE NOCASE ASC")
    fun getAllMealsFlow(): Flow<List<MealEntity>>

    @Query("SELECT * FROM meals ORDER BY name COLLATE NOCASE ASC")
    suspend fun getAllMeals(): List<MealEntity>

    // Tìm kiếm meals theo tên
    @Query("""
        SELECT * FROM meals 
        WHERE name LIKE '%' || :query || '%' 
        ORDER BY name COLLATE NOCASE ASC
    """)
    fun searchMealsByNameFlow(query: String): Flow<List<MealEntity>>

    @Query("""
        SELECT * FROM meals 
        WHERE CAST(calories AS TEXT) LIKE :caloriesQuery || '%'
        ORDER BY calories ASC
    """)
    fun searchMealsByCaloriesFlow(caloriesQuery: String): Flow<List<MealEntity>>

    // Lấy meals theo list IDs (cho favorites)
    @Query("SELECT * FROM meals WHERE id IN (:mealIds)")
    suspend fun getMealsByIds(mealIds: List<String>): List<MealEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM meals WHERE id = :mealId)")
    suspend fun mealExists(mealId: String): Boolean

    @Query("DELETE FROM meals WHERE id = :mealId")
    suspend fun deleteMealById(mealId: String)

    // Xóa tất cả meals
    @Query("DELETE FROM meals")
    suspend fun deleteAllMeals()

    @Update
    suspend fun updateMeal(meal: MealEntity)
}
