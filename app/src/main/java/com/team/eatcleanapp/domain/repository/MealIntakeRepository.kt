package com.team.eatcleanapp.domain.repository

import com.team.eatcleanapp.domain.model.MealIntake
import com.team.eatcleanapp.util.Result
import java.util.Date

interface MealIntakeRepository {
    // lấy danh sách MealIntake theo userId và ngày
    suspend fun getMealIntakeByDate(userId: String, date: Date): Result<List<MealIntake>>

    // lấy MealIntake theo ngày và buổi ăn (category)
    suspend fun getMealIntakeByDateAndCategory(
        userId: String,
        date: Date,
        category: String
    ): Result<List<MealIntake>>

    // Lấy theo mealId
    suspend fun getMealIntakeByMealId(
        userId: String,
        date: Date,
        mealId: String,
        category: String
    ): Result<MealIntake?>

    // Cập nhật MealIntake
    suspend fun upsertMealIntake(mealIntake: MealIntake): Result<Unit>

    // cập nhật trạng thái ăn/chưa ăn theo id
    suspend fun updateCheckedStatus(id: String, isChecked: Boolean): Result<Unit>

    // Update theo dailymenuitemid
    suspend fun updateCheckedStatusByDailyMenuItemId(
        dailyMenuItemId: String,
        isChecked: Boolean
    ): Result<Unit>

    // xóa toàn bộ dữ liệu MealIntake trong local database
    suspend fun clearLocalData(): Result<Unit>

    // xóa toàn bộ dữ liệu MealIntake của một user cụ thể trong local database
    suspend fun clearLocalDataByUserId(userId: String): Result<Unit>
}