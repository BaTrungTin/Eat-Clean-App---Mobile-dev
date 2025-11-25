package com.team.eatcleanapp.domain.repository

import com.team.eatcleanapp.data.local.entities.MealOverrideEntity
import com.team.eatcleanapp.domain.model.meal.Meal
import com.team.eatcleanapp.util.Result

interface MealOverrideRepository {

    // Lấy meal override từ local (Room)
    suspend fun getMealOverride(userId: String, mealId: String): Result<MealOverrideEntity?>

    // Lưu hoặc cập nhật meal override
    suspend fun saveMealOverride(
        userId: String,
        originalMeal: Meal,
        modifiedMeal: Meal
    ): Result<Unit>

    // Xóa meal override khi user bỏ favorite
    suspend fun removeMealOverride(userId: String, mealId: String): Result<Unit>

    // Kiểm tra meal có override không
    suspend fun hasMealOverride(userId: String, mealId: String): Result<Boolean>

    // Áp dụng override vào meal (trả về Meal đã override nếu có)
    suspend fun getMealWithOverride(userId: String, meal: Meal): Result<Meal>
}
