package com.team.eatcleanapp.data.local.dao

import androidx.room.*
import com.team.eatcleanapp.data.local.entities.MealOverrideEntity

@Dao
interface MealOverrideDao {

    // Lấy meal override cụ thể
    @Query("""
        SELECT * FROM meal_override 
        WHERE userId = :userId AND mealId = :mealId
        LIMIT 1
    """)
    suspend fun getMealOverride(userId: String, mealId: String): MealOverrideEntity?

    // Thêm hoặc cập nhật meal override (khi user sửa món từ favorite)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMealOverride(override: MealOverrideEntity)

    // Xóa meal override (khi bỏ favorite)
    @Query("DELETE FROM meal_override WHERE userId = :userId AND mealId = :mealId")
    suspend fun deleteMealOverride(userId: String, mealId: String)

    // Kiểm tra meal có override không
    @Query("SELECT EXISTS(SELECT 1 FROM meal_override WHERE userId = :userId AND mealId = :mealId)")
    suspend fun mealOverrideExists(userId: String, mealId: String): Boolean
}