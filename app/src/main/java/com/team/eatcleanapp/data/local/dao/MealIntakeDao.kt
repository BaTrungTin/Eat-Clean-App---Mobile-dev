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
    """)
    suspend fun getMealIntakeByDate(
        userId: String,
        date: String
    ): List<MealIntakeEntity>

    // Ghi nhận / cập nhật log ăn uống (ăn / chưa ăn ... )
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMealIntake(entity: MealIntakeEntity)
}