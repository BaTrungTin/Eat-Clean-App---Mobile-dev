package com.team.eatcleanapp.domain.repository

import com.team.eatcleanapp.domain.model.dailymenu.DailyMenuItem
import com.team.eatcleanapp.domain.model.dailymenu.DateCategory
import com.team.eatcleanapp.util.Result
import java.util.Date

interface DailyMenuRepository {
    // lấy một dailymenuitem theo id
    suspend fun getDailyMenuItemById(id: String): Result<DailyMenuItem?>

    // Lấy danh sách DailyMenuItem theo userId và ngày
    suspend fun getDailyMenuByDate(userId: String, date: Date): List<DailyMenuItem>

    // Lấy danh sách DailyMenuItem trong khoảng tuần (startDate đến endDate)
    suspend fun getDailyMenuByWeek(userId: String, startDate: Date, endDate: Date): Result<List<DailyMenuItem>>

    // Lấy danh sách DailyMenuItem theo buổi ăn (category) trong ngày
    suspend fun getMealsByCategory(userId: String, date: Date, category: String): List<DailyMenuItem>

    // Tính tổng calo của một ngày cho userId
    suspend fun getTotalCaloriesByDate(userId: String, date: Date): Result<Double?>

    // Tính tổng calo của một buổi ăn trong ngày
    suspend fun getTotalCaloriesByCategory(userId: String, date: Date, category: String): Result<Double?>

    // Kiểm tra xem một mealId có tồn tại trong daily menu của userId, ngày và category không
    suspend fun mealExists(userId: String, date: Date, mealId: String, category: String): Result<Boolean>

    // Đếm số lượng món ăn trong một buổi ăn của ngày
    suspend fun getMealCountByCategory(userId: String, date: Date, category: String): Result<Int>

    // Thêm một DailyMenuItem vào daily menu
    suspend fun insertDailyMenu(dailyMenuItem: DailyMenuItem): Result<Unit>

    // Thêm nhiều DailyMenuItem cùng lúc
    suspend fun insertMultipleDailyMenus(dailyMenuItems: List<DailyMenuItem>): Result<Unit>

    // Cập nhật thông tin một DailyMenuItem
    suspend fun updateDailyMenu(dailyMenuItem: DailyMenuItem): Result<Unit>

    // Cập nhật phần ăn (quantity) của 1 món
    suspend fun updateQuantity(id: String, newQuantity: Double): Result<Unit>

    // Xóa một DailyMenuItem theo id
    suspend fun deleteDailyMenuItem(id: String): Result<Unit>

    // Xóa tất cả DailyMenuItem của một user trong một ngày
    suspend fun deleteAllByDate(userId: String, date: Date): Result<Unit>

    // Xóa 1 buổi của 1 ngày
    suspend fun deleteCategory(userId: String, date: Date, category: String): Result<Unit>

    // Xóa nhiều buổi của 1 ngày
    suspend fun deleteMultipleCategoriesOfDate(userId: String, date: Date, categories: List<String>): Result<Unit>

    // Xóa các DailyMenuItem theo danh sách cặp ngày + category
    suspend fun deleteSelectedCategories(userId: String, selectedDateCategories: List<DateCategory>): Result<Unit>

    // Xóa tất cả DailyMenuItem của một user trong danh sách ngày
    suspend fun deleteSelectedDates(userId: String, selectedDates: List<Date>): Result<Unit>

    // Xóa tất cả DailyMenuItem của một user trong khoảng tuần
    suspend fun deleteWeek(userId: String, weekStartDate: Date, weekEndDate: Date): Result<Unit>

    // Cập nhật tên và calo của món ăn khi dữ liệu từ realtime db thay đổi
    suspend fun updateDailyMenuMealInfo(mealId: String, newMealName: String, newCalories: Double): Result<Unit>

    // Xóa tất cả DailyMenuItem có mealId cụ thể (khi món bị xóa từ realtime db)
    suspend fun deleteDailyMenuByMealId(mealId: String): Result<Unit>

    // Lấy danh sách tất cả mealId đang được dùng trong daily menu (để check khi sync)
    suspend fun getAllUsedMealIds(): Result<List<String>>

    // Xóa toàn bộ daily menu của một user
    suspend fun deleteAllByUserId(userId: String): Result<Unit>
}