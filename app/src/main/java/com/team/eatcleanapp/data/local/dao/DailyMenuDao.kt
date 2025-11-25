package com.team.eatcleanapp.data.local.dao

import androidx.room.*
import com.team.eatcleanapp.data.local.entities.DailyMenuEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyMenuDao {

    // Lấy thực đơn hàng ngày cho 1 ngày cụ thể (Flow)
    @Query("""
        SELECT * FROM daily_menu 
        WHERE userId = :userId AND date = :date 
        ORDER BY mealType, createdAt
    """)
    fun getDailyMenuByDate(userId: String, date: Long): Flow<List<DailyMenuEntity>>

    // Lấy thực đơn hàng ngày cho 1 ngày cụ thể (Suspend)
    @Query("""
        SELECT * FROM daily_menu 
        WHERE userId = :userId AND date = :date 
        ORDER BY mealType, createdAt
    """)
    suspend fun getDailyMenuByDateSuspend(userId: String, date: Long): List<DailyMenuEntity>

    // Lấy thực đơn hàng tuần (7 ngày, từ startDate đến endDate)
    @Query("""
        SELECT * FROM daily_menu 
        WHERE userId = :userId 
        AND date >= :startDate 
        AND date <= :endDate 
        ORDER BY date, mealType, createdAt
    """)
    fun getWeeklyMenu(userId: String, startDate: Long, endDate: Long): Flow<List<DailyMenuEntity>>

    // Lấy thực đơn hàng tuần (Suspend)
    @Query("""
        SELECT * FROM daily_menu 
        WHERE userId = :userId 
        AND date >= :startDate 
        AND date <= :endDate 
        ORDER BY date, mealType, createdAt
    """)
    suspend fun getWeeklyMenuSuspend(userId: String, startDate: Long, endDate: Long): List<DailyMenuEntity>

    // Lấy món ăn theo mealType (BREAKFAST, LUNCH, DINNER, SNACK)
    @Query("""
        SELECT * FROM daily_menu 
        WHERE userId = :userId 
        AND date = :date 
        AND mealType = :mealType
        ORDER BY createdAt
    """)
    suspend fun getMealsByType(userId: String, date: Long, mealType: String): List<DailyMenuEntity>

    // Lấy một món ăn cụ thể theo composite key
    @Query("""
        SELECT * FROM daily_menu 
        WHERE userId = :userId 
        AND date = :date 
        AND mealId = :mealId 
        AND mealType = :mealType
        LIMIT 1
    """)
    suspend fun getDailyMenuItem(
        userId: String,
        date: Long,
        mealId: String,
        mealType: String
    ): DailyMenuEntity?

    // Thêm 1 món ăn vào thực đơn
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: DailyMenuEntity)

    // Thêm nhiều món ăn vào thực đơn
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeals(meals: List<DailyMenuEntity>)

    // Cập nhật món ăn
    @Update
    suspend fun updateMeal(meal: DailyMenuEntity)

    // Cập nhật portionSize
    @Query("""
        UPDATE daily_menu 
        SET portionSize = :portionSize, 
            updatedAt = :updatedAt
        WHERE userId = :userId 
        AND date = :date 
        AND mealId = :mealId 
        AND mealType = :mealType
    """)
    suspend fun updatePortionSize(
        userId: String,
        date: Long,
        mealId: String,
        mealType: String,
        portionSize: Double,
        updatedAt: Long = System.currentTimeMillis()
    )

    // Cập nhật thông tin món ăn khi meal master data bị thay đổi
    @Query("""
        UPDATE daily_menu 
        SET mealName = :newMealName, 
            calories = :newCalories, 
            updatedAt = :updatedAt
        WHERE mealId = :mealId
    """)
    suspend fun updateMealInfo(
        mealId: String,
        newMealName: String,
        newCalories: Double,
        updatedAt: Long = System.currentTimeMillis()
    )

    // Lấy tổng calories planned của 1 ngày
    @Query("""
        SELECT SUM(calories * portionSize) 
        FROM daily_menu 
        WHERE userId = :userId 
        AND date = :date
    """)
    suspend fun getTotalCaloriesByDate(userId: String, date: Long): Double?

    // Lấy tổng calories theo mealType trong 1 ngày
    @Query("""
        SELECT SUM(calories * portionSize) 
        FROM daily_menu 
        WHERE userId = :userId 
        AND date = :date 
        AND mealType = :mealType
    """)
    suspend fun getTotalCaloriesByMealType(
        userId: String,
        date: Long,
        mealType: String
    ): Double?

    // Đếm số món trong một mealType
    @Query("""
        SELECT COUNT(*) 
        FROM daily_menu 
        WHERE userId = :userId 
        AND date = :date 
        AND mealType = :mealType
    """)
    suspend fun getMealCountByType(userId: String, date: Long, mealType: String): Int

    // Đếm tổng số món trong một ngày
    @Query("""
        SELECT COUNT(*) 
        FROM daily_menu 
        WHERE userId = :userId 
        AND date = :date
    """)
    suspend fun getMealCountByDate(userId: String, date: Long): Int

    // Kiểm tra món ăn đã tồn tại trong thực đơn chưa
    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM daily_menu 
            WHERE userId = :userId 
            AND date = :date
            AND mealId = :mealId 
            AND mealType = :mealType
        )
    """)
    suspend fun mealExists(
        userId: String,
        date: Long,
        mealId: String,
        mealType: String
    ): Boolean

    // Lấy tất cả mealIds đang được sử dụng (để sync hoặc validate)
    @Query("SELECT DISTINCT mealId FROM daily_menu WHERE userId = :userId")
    suspend fun getAllUsedMealIds(userId: String): List<String>

    // Lấy tất cả dates có thực đơn
    @Query("""
        SELECT DISTINCT date 
        FROM daily_menu 
        WHERE userId = :userId 
        ORDER BY date ASC
    """)
    suspend fun getAllDatesWithMenu(userId: String): List<Long>

    // Xóa món ăn cụ thể
    @Delete
    suspend fun deleteMeal(meal: DailyMenuEntity)

    // Xóa món ăn theo mealId (khi meal bị xóa khỏi master data)
    @Query("DELETE FROM daily_menu WHERE mealId = :mealId")
    suspend fun deleteMealsByMealId(mealId: String)

    // Xóa món ăn cụ thể theo composite primary key
    // Returns number of rows deleted
    @Query("""
        DELETE FROM daily_menu 
        WHERE userId = :userId 
        AND date = :date 
        AND mealId = :mealId 
        AND mealType = :mealType
    """)
    suspend fun deleteDailyMenuItem(
        userId: String,
        date: Long,
        mealId: String,
        mealType: String
    ): Int

    // Xóa tất cả món của 1 ngày
    @Query("DELETE FROM daily_menu WHERE userId = :userId AND date = :date")
    suspend fun deleteAllByDate(userId: String, date: Long)

    // Xóa tất cả món của 1 mealType trong 1 ngày
    @Query("""
        DELETE FROM daily_menu 
        WHERE userId = :userId 
        AND date = :date 
        AND mealType = :mealType
    """)
    suspend fun deleteMealTypeOfDate(userId: String, date: Long, mealType: String)

    // Xóa nhiều mealTypes trong 1 ngày
    @Query("""
        DELETE FROM daily_menu
        WHERE userId = :userId 
        AND date = :date 
        AND mealType IN (:mealTypes)
    """)
    suspend fun deleteMultipleMealTypesOfDate(
        userId: String,
        date: Long,
        mealTypes: List<String>
    )

    // Xóa nhiều ngày
    @Query("""
        DELETE FROM daily_menu 
        WHERE userId = :userId 
        AND date IN (:dates)
    """)
    suspend fun deleteMultipleDates(userId: String, dates: List<Long>)

    // Xóa thực đơn trong khoảng thời gian
    @Query("""
        DELETE FROM daily_menu 
        WHERE userId = :userId 
        AND date >= :startDate 
        AND date <= :endDate
    """)
    suspend fun deleteMenuByDateRange(userId: String, startDate: Long, endDate: Long)

    // Xóa tất cả thực đơn của user
    @Query("DELETE FROM daily_menu WHERE userId = :userId")
    suspend fun deleteAllByUserId(userId: String)
}