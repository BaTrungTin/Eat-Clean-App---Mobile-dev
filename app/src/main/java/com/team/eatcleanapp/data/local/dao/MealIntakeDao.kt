package com.team.eatcleanapp.data.local.dao

import androidx.room.*
import com.team.eatcleanapp.data.local.entities.MealIntakeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MealIntakeDao {

    // Lấy meal intake theo ngày
    @Query("""
        SELECT * FROM meal_intake
        WHERE userId = :userId 
        AND date = :date
        ORDER BY mealType
    """)
    suspend fun getMealIntakeByDate(userId: String, date: Long): List<MealIntakeEntity>

    // Lấy meal intake theo ngày (Flow)
    @Query("""
        SELECT * FROM meal_intake
        WHERE userId = :userId 
        AND date = :date
        ORDER BY mealType
    """)
    fun getMealIntakeByDateFlow(userId: String, date: Long): Flow<List<MealIntakeEntity>>

    // Lấy meal intake theo mealType trong một ngày
    @Query("""
        SELECT * FROM meal_intake
        WHERE userId = :userId 
        AND date = :date
        AND mealType = :mealType
        ORDER BY id
    """)
    suspend fun getMealIntakeByMealType(
        userId: String,
        date: Long,
        mealType: String
    ): List<MealIntakeEntity>

    // Lấy meal intake theo ID
    @Query("SELECT * FROM meal_intake WHERE id = :id LIMIT 1")
    suspend fun getMealIntakeById(id: String): MealIntakeEntity?

    // Lấy meal intake theo dailyMenuItemId
    @Query("""
        SELECT * FROM meal_intake 
        WHERE dailyMenuItemId = :dailyMenuItemId
        LIMIT 1
    """)
    suspend fun getMealIntakeByDailyMenuId(dailyMenuItemId: String): MealIntakeEntity?

    // Thêm hoặc cập nhật meal intake
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMealIntake(entity: MealIntakeEntity)

    // Thêm hoặc cập nhật nhiều meal intakes
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMealIntakes(entities: List<MealIntakeEntity>)

    // Cập nhật meal intake (khi tick ô consumed)
    @Update
    suspend fun updateMealIntake(entity: MealIntakeEntity)

    // Cập nhật trạng thái consumed (khi tick/untick)
    @Query("""
        UPDATE meal_intake 
        SET isConsumed = :isConsumed
        WHERE id = :id
    """)
    suspend fun updateConsumedStatus(id: String, isConsumed: Boolean)

    // Cập nhật portionSize và totalCalories
    @Query("""
        UPDATE meal_intake 
        SET portionSize = :portionSize,
            totalCalories = :totalCalories
        WHERE id = :id
    """)
    suspend fun updatePortion(id: String, portionSize: Double, totalCalories: Double)

    // Lấy tổng calories đã consumed trong một ngày (cho progress circles)
    @Query("""
        SELECT SUM(totalCalories) 
        FROM meal_intake
        WHERE userId = :userId 
        AND date = :date
        AND isConsumed = 1
    """)
    suspend fun getTotalConsumedCalories(userId: String, date: Long): Double?

    // Lấy tổng calories planned (cả consumed và chưa consumed)
    @Query("""
        SELECT SUM(totalCalories) 
        FROM meal_intake
        WHERE userId = :userId 
        AND date = :date
    """)
    suspend fun getTotalPlannedCalories(userId: String, date: Long): Double?

    // Lấy số lượng meals đã consumed
    @Query("""
        SELECT COUNT(*) 
        FROM meal_intake
        WHERE userId = :userId 
        AND date = :date
        AND isConsumed = 1
    """)
    suspend fun getConsumedMealCount(userId: String, date: Long): Int

    // Lấy tổng số meals (cả consumed và chưa)
    @Query("""
        SELECT COUNT(*) 
        FROM meal_intake
        WHERE userId = :userId 
        AND date = :date
    """)
    suspend fun getTotalMealCount(userId: String, date: Long): Int

    // Kiểm tra meal intake có tồn tại không
    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM meal_intake 
            WHERE dailyMenuItemId = :dailyMenuItemId
        )
    """)
    suspend fun mealIntakeExists(dailyMenuItemId: String): Boolean

    // Lấy % calories consumed so với planned
    @Query("""
        SELECT 
            (SUM(CASE WHEN isConsumed = 1 THEN totalCalories ELSE 0 END) * 100.0) / 
            NULLIF(SUM(totalCalories), 0)
        FROM meal_intake
        WHERE userId = :userId 
        AND date = :date
    """)
    suspend fun getCaloriesProgressPercentage(userId: String, date: Long): Double?

    // Lấy tất cả dates có meal intake
    @Query("""
        SELECT DISTINCT date 
        FROM meal_intake 
        WHERE userId = :userId 
        ORDER BY date DESC
    """)
    suspend fun getAllDatesWithIntake(userId: String): List<Long>

    // Xóa meal intake
    @Delete
    suspend fun deleteMealIntake(entity: MealIntakeEntity)

    // Xóa meal intake theo ID
    @Query("DELETE FROM meal_intake WHERE id = :id")
    suspend fun deleteMealIntakeById(id: String)

    // Xóa meal intakes theo dailyMenuItemId
    @Query("DELETE FROM meal_intake WHERE dailyMenuItemId = :dailyMenuItemId")
    suspend fun deleteMealIntakeByDailyMenuId(dailyMenuItemId: String)

    // Xóa tất cả meal intakes của một ngày
    @Query("DELETE FROM meal_intake WHERE userId = :userId AND date = :date")
    suspend fun deleteAllByDate(userId: String, date: Long)

    // Xóa tất cả meal intakes của user
    @Query("DELETE FROM meal_intake WHERE userId = :userId")
    suspend fun deleteAllByUserId(userId: String)
}