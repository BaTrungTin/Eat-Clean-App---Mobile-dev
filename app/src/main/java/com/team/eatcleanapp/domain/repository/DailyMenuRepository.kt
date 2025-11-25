package com.team.eatcleanapp.domain.repository

import com.team.eatcleanapp.domain.model.dailymenu.DailyMenuItem
import com.team.eatcleanapp.domain.model.enums.MealCategory
import com.team.eatcleanapp.util.Result
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface DailyMenuRepository {

    // Lấy danh sách món ăn theo ngày (theo dõi real-time)
    fun getDailyMenuByDate(userId: String, date: Date): Flow<Result<List<DailyMenuItem>>>

    // Lấy menu trong khoảng 1 tuần (hoặc nhiều ngày)
    suspend fun getWeeklyMenu(
        userId: String,
        startDate: Date,
        endDate: Date
    ): Result<List<DailyMenuItem>>

    // Lấy 1 món cụ thể theo: userId + date + mealType + mealId
    suspend fun getDailyMenuItem(
        userId: String,
        date: Date,
        mealId: String,
        mealType: MealCategory
    ): Result<DailyMenuItem?>

    // Insert 1 hoặc nhiều DailyMenuItem vào DB
    suspend fun insertDailyMenus(items: List<DailyMenuItem>): Result<Unit>

    // Cập nhật toàn bộ thông tin của một món trong Daily Menu
    // (bao gồm portion, calories, mealName, mealType...)
    suspend fun updateDailyMenu(item: DailyMenuItem): Result<Unit>

    suspend fun deleteSpecificMeal(
        userId: String,
        date: Date,
        mealId: String,
        mealType: MealCategory
    ): Result<Unit>

    /** Xóa menu theo date + mealType
     * value = listOf("breakfast") → xóa đúng buổi đó
     * - value = listOf("lunch","dinner") → xóa nhiều buổi
     * - value = null → xóa toàn bộ các buổi của ngày đó
     * => xử lý được mọi trường hợp: 1 buổi, nhiều buổi, nhiều ngày, cả tuần
     */
    suspend fun deleteMeals(
        userId: String,
        dateMealTypes: Map<Date, List<MealCategory>?>
    ): Result<Unit>

    // Tính tổng calories của 1 ngày
    suspend fun getTotalCaloriesByDate(
        userId: String,
        date: Date
    ): Result<Double?>

    // Tính tổng calories của 1 buổi (breakfast, lunch,...)
    suspend fun getTotalCaloriesByType(
        userId: String,
        date: Date,
        mealType: MealCategory
    ): Result<Double?>

    // Đếm số lượng món ăn trong 1 buổi của ngày
    suspend fun getMealCountByType(
        userId: String,
        date: Date,
        mealType: MealCategory
    ): Result<Int>

    // Cập nhật tên và calories của món khi dữ liệu trong Firebase thay đổi
    suspend fun updateDailyMenuMealInfo(
        mealId: String,
        newMealName: String,
        newCalories: Double
    ): Result<Unit>

    // Kiểm tra xem 1 mealId có tồn tại trong daily menu của user hay không
    suspend fun mealExistsInMenu(
        userId: String,
        date: Date,
        mealId: String,
        mealType: MealCategory
    ): Result<Boolean>

    // Xóa tất cả DailyMenuItem có mealId cụ thể (khi món bị xóa khỏi Firestore)
    suspend fun deleteDailyMenuItemByMealId(mealId: String): Result<Unit>

    // Xóa toàn bộ daily menu của một user (dùng khi user reset app hoặc xóa tài khoản)
    suspend fun deleteAllByUserId(userId: String): Result<Unit>

    // Lấy danh sách tất cả mealId hiện đang được sử dụng (để so sánh khi sync)
    suspend fun getAllUsedMealIds(userId: String): Result<List<String>>
}
