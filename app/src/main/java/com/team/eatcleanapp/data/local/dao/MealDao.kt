package com.team.eatcleanapp.data.local.dao

import androidx.room.*
import com.team.eatcleanapp.data.local.entities.MealEntity

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: MealEntity)

    @Query("SELECT * FROM meals WHERE id = :mealId")
    suspend fun getMealById(mealId: Long): MealEntity?

    @Query("SELECT * FROM meals ORDER BY name COLLATE NOCASE ASC")
    suspend fun getAllMeals(): List<MealEntity>

    @Query("SELECT * FROM meals WHERE name LIKE '%' || :query || '%' OR calories LIKE '%' || :query || '%' ORDER BY name COLLATE NOCASE ASC")
    suspend fun searchMeals(query: String): List<MealEntity>
}
