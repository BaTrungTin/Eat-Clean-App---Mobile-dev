package com.team.eatcleanapp.domain.repository

import com.team.eatcleanapp.domain.model.nutrition.MealIntake
import com.team.eatcleanapp.util.Result
import java.util.Date

interface MealIntakeRepository {

    // lay meal intake theo ngay
    suspend fun getMealIntakeByDate(userId: String, date: Date): Result<List<MealIntake>>

    // lay meal intake theo id
    suspend fun getMealIntakeById(id: String): Result<MealIntake?>

    // luu hoac cap nhat meal intake
    suspend fun saveMealIntake(mealIntake: MealIntake): Result<Unit>

    // cap nhat trang thai consumed
    suspend fun updateConsumedStatus(id: String, isConsumed: Boolean): Result<Unit>

    // cap nhat portion size
    suspend fun updatePortionSize(id: String, portionSize: Double, totalCalories: Double): Result<Unit>

    // tinh tong calories da consumed trong ngay
    suspend fun getTotalConsumedCalories(userId: String, date: Date): Result<Double?>

    // tinh tong calories planned trong ngay
    suspend fun getTotalPlannedCalories(userId: String, date: Date): Result<Double>

    // tinh % calories progress
    suspend fun getCaloriesProgressPercentage(
        userId: String,
        date: Date
    ): Result<Double?>

    // xoa meal intake
    suspend fun deleteMealIntake(id: String): Result<Unit>

    // xoa tat ca meal intake theo ngay
    suspend fun deleteAllMealIntakeByDate(
        userId: String,
        date: Date
    ): Result<Unit>

    suspend fun deleteAllByUserId(userId: String): Result<Unit>
}