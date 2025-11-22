package com.team.eatcleanapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.team.eatcleanapp.data.local.entities.MealIntakeEntity

// Dao cho bảng meal_intake
@Dao
interface MealIntakeDao {

    @Query("""
        SELECT * FROM meal_intake
        WHERE userId = :userId AND date = :date
        ORDER BY 
            CASE category
                WHEN 'BREAKFAST' THEN 1
                WHEN 'LUNCH' THEN 2  
                WHEN 'DINNER' THEN 3
                ELSE 4
            END
    """)
    suspend fun getMealIntakeByDate(
        userId: String,
        date: String
    ): List<MealIntakeEntity>

    @Query("""
        SELECT * FROM meal_intake 
        WHERE userId = :userId 
        AND date = :date 
        AND category = :category
    """)
    suspend fun getMealIntakeByDateAndCategory(
        userId: String,
        date: String,
        category: String
    ): List<MealIntakeEntity>

    // Lấy theo mealId
    @Query("""
        SELECT * FROM meal_intake 
        WHERE userId = :userId 
        AND date = :date 
        AND mealId = :mealId
        AND category = :category
    """)
    suspend fun getMealIntakeByMealId(
        userId: String,
        date: String,
        mealId: String,
        category: String
    ): MealIntakeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: MealIntakeEntity)

    @Query("UPDATE meal_intake SET isChecked = :isChecked WHERE id = :id")
    suspend fun updateCheckedStatus(id: String, isChecked: Boolean)

    // Update theo dailymenuitemId
    @Query("""
        UPDATE meal_intake 
        SET isChecked = :isChecked 
        WHERE dailyMenuItemId = :dailyMenuItemId
    """)
    suspend fun updateCheckedStatusByDailyMenuItemId(
        dailyMenuItemId: String,
        isChecked: Boolean
    )

    @Query("DELETE FROM meal_intake")
    suspend fun clearAllLocalData()

    // Xóa theo user
    @Query("DELETE FROM meal_intake WHERE userId = :userId")
    suspend fun clearLocalDataByUserId(userId: String)
}