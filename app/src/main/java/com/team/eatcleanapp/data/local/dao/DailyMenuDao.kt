package com.team.eatcleanapp.data.local.dao

import androidx.room.*
import com.team.eatcleanapp.data.local.entities.DailyMenuEntity

@Dao
interface DailyMenuDao {

    // Lấy thực đơn hàng ngày cho 1 ngày cụ thể
    @Query("""
        SELECT * FROM daily_menu 
        WHERE userId = :userId AND date = :date 
        ORDER BY category
    """)
    suspend fun getDailyMenuByDate(userId: String, date: String): List<DailyMenuEntity>

    // Lấy thực đơn hàng tuần (7 ngày, từ t2 - cn)
    @Query("""
        SELECT * FROM daily_menu 
        WHERE userId = :userId 
        AND date BETWEEN :startDate AND :endDate 
        ORDER BY date, category
    """)
    suspend fun getDailyMenusByDateRange(userId: String, startDate: String, endDate: String):
            List<DailyMenuEntity>

    // Lấy món ăn theo buổi
    @Query("""
        SELECT * FROM daily_menu 
        WHERE userId = :userId 
        AND date = :date 
        AND category = :category
    """)
    suspend fun getMealsByCategory(userId: String, date: String, category: String): List<DailyMenuEntity>

    // Lấy món ăn theo ID
    @Query("SELECT * FROM daily_menu WHERE id = :id")
    suspend fun getDailyMenuItemById(id: String): DailyMenuEntity?

    // Thêm 1 món ăn
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: DailyMenuEntity)

    // Thêm nhiều món ăn
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeals(meals: List<DailyMenuEntity>)

    // Cập nhật món
    @Update
    suspend fun updateMeal(meal: DailyMenuEntity)

    // Lấy tổng calo của 1 ngày
    @Query("""
        SELECT SUM(calories * quantity) 
        FROM daily_menu 
        WHERE userId = :userId 
        AND date = :date
    """)
    suspend fun getTotalCaloriesByDate(userId: String, date: String): Double?

    // Tổng kcal của 1 buổi trong 1 ngày cụ thể
    @Query("""
        SELECT SUM(calories * quantity) 
        FROM daily_menu 
        WHERE userId = :userId 
        AND date = :date 
        AND category = :category
    """)
    suspend fun getTotalCaloriesByCategoryAndDate(
        userId: String,
        date: String,
        category: String
    ): Double?

    // Đếm số món trong 1 buổi của 1 ngày
    @Query("""
        SELECT COUNT(*) FROM daily_menu 
        WHERE userId = :userId 
        AND date = :date 
        AND category = :category
    """)
    suspend fun getMealCountByCategoryAndDate(
        userId: String,
        date: String,
        category: String
    ): Int

    // Kiểm tra xem món ăn có trong thực đơn không
    @Query("""
        SELECT COUNT(*) FROM daily_menu 
        WHERE userId = :userId AND date = :date
        AND mealId = :mealId AND category = :category
    """)
    suspend fun mealExists(userId: String, date: String, mealId: String, category: String): Int

    // Cập nhật daily menu khi meal từ realtime db thay đổi
    @Query("""
        UPDATE daily_menu 
        SET 
            mealName = :newMealName,
            calories = :newCalories
        WHERE mealId = :mealId
    """)
    suspend fun updateDailyMenuMealInfo(
        mealId: String,
        newMealName: String,
        newCalories: Double
    )

    // Cập nhật phần ăn (quantity) của 1 món
    @Query("""
        UPDATE daily_menu 
        SET quantity = :newQuantity
        WHERE id = :id
    """)
    suspend fun updateQuantity(id: String, newQuantity: Double)

    // Xóa daily menu items khi meal bị xóa từ realtime db
    @Query("DELETE FROM daily_menu WHERE mealId = :mealId")
    suspend fun deleteDailyMenuByMealId(mealId: String)

    // Lấy tất cả mealIds đang được dùng trong daily menu (để check khi sync)
    @Query("SELECT DISTINCT mealId FROM daily_menu")
    suspend fun getAllUsedMealIds(): List<String>

    // Xóa bữa ăn cụ thể
    @Delete
    suspend fun deleteMeal(meal: DailyMenuEntity)

    // Xóa 1 món
    @Query("DELETE FROM daily_menu WHERE id = :id")
    suspend fun deleteMealById(id: String)

    // Xóa tất cả buổi của 1 ngày
    @Query("DELETE FROM daily_menu WHERE userId = :userId AND date = :date")
    suspend fun deleteAllByDate(userId: String, date: String)

    // Xóa 1 buổi của 1 ngày
    @Query("""
        DELETE FROM daily_menu
        WHERE userId = :userId AND date = :date AND category = :category
    """)
    suspend fun deleteCategoryOfDate(
        userId: String,
        date: String,
        category: String
    )

    // Xóa nhiều buổi của 1 ngày
    @Query("""
        DELETE FROM daily_menu
        WHERE userId = :userId AND date = :date AND category IN (:categories)
    """)
    suspend fun deleteMultipleCategoriesOfDate(
        userId: String,
        date: String,
        categories: List<String>
    )

    // Xóa nhiều buổi ở nhiều ngày khác nhau
    @Query("""
        DELETE FROM daily_menu
        WHERE userId = :userId
          AND date IN (:dates)
          AND category IN (:categories)
    """)
    suspend fun deleteCategoriesAcrossDates(
        userId: String,
        dates: List<String>,
        categories: List<String>
    )

    // Xóa nhiều ngày khác nhau
    @Query("DELETE FROM daily_menu WHERE userId = :userId AND date IN (:dates)")
    suspend fun deleteSelectedDates(userId: String, dates: List<String>)

    // Xóa tất cả của user
    @Query("DELETE FROM daily_menu WHERE userId = :userId")
    suspend fun deleteAllByUserId(userId: String)
}