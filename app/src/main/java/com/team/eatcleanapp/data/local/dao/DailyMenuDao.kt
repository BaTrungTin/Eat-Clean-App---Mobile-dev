package com.team.eatcleanapp.data.local.dao

import androidx.room.*
import com.team.eatcleanapp.data.local.entities.DailyMenuEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyMenuDao {
    
    // Get daily menu for a specific date
    @Query("SELECT * FROM daily_menu WHERE userId = :userId AND date = :date ORDER BY mealType, createdAt")
    fun getMenuByDate(userId: String, date: Long): Flow<List<DailyMenuEntity>>
    
    // Get daily menu for a specific date (suspend function)
    @Query("SELECT * FROM daily_menu WHERE userId = :userId AND date = :date ORDER BY mealType, createdAt")
    suspend fun getDailyMenuByDateSuspend(userId: String, date: Long): List<DailyMenuEntity>
    
    // Get weekly menu (7 days from start date)
    @Query("""
        SELECT * FROM daily_menu 
        WHERE userId = :userId 
        AND date >= :startDate 
        AND date < :endDate 
        ORDER BY date, mealType, createdAt
    """)
    fun getWeeklyMenu(userId: String, startDate: Long, endDate: Long): Flow<List<DailyMenuEntity>>
    
    // Get weekly menu (suspend function)
    @Query("""
        SELECT * FROM daily_menu 
        WHERE userId = :userId 
        AND date >= :startDate 
        AND date < :endDate 
        ORDER BY date, mealType, createdAt
    """)
    suspend fun getWeeklyMenuSuspend(userId: String, startDate: Long, endDate: Long): List<DailyMenuEntity>
    
    // Get meals by meal type for a specific date
    @Query("""
        SELECT * FROM daily_menu 
        WHERE userId = :userId 
        AND date = :date 
        AND mealType = :mealType 
        ORDER BY createdAt
    """)
    suspend fun getMealsByType(userId: String, date: Long, mealType: String): List<DailyMenuEntity>
    
    // Insert single meal
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: DailyMenuEntity): Long
    
    // Insert multiple meals
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeals(meals: List<DailyMenuEntity>): List<Long>

    // Update meal intake
    @Query("UPDATE daily_menu SET isConsumed = :isConsumed WHERE id = :mealIntakeId")
    suspend fun updateMealIntake(mealIntakeId: Long, isConsumed: Boolean)
    
    // Delete specific meal
    @Delete
    suspend fun deleteMeal(meal: DailyMenuEntity)
    
    @Query("DELETE FROM daily_menu WHERE id = :mealId")
    suspend fun deleteMealById(mealId: Long)
    
    // Delete all meals for a specific date
    @Query("DELETE FROM daily_menu WHERE userId = :userId AND date = :date")
    suspend fun deleteAllMealsByDate(userId: String, date: Long)
    
    // Delete all meals for a user
    @Query("DELETE FROM daily_menu WHERE userId = :userId")
    suspend fun deleteAllMealsByUserId(userId: String)
    
    // Get total calories for a date
    @Query("SELECT SUM(calories) FROM daily_menu WHERE userId = :userId AND date = :date")
    suspend fun getTotalCaloriesByDate(userId: String, date: Long): Double?
}

