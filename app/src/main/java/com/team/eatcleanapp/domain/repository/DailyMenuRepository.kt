package com.team.eatcleanapp.domain.repository

import com.team.eatcleanapp.domain.model.DailyMenu
import com.team.eatcleanapp.util.Result
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface DailyMenuRepository {
    /**
     * Get daily menu for a specific date
     */
    fun getDailyMenu(userId: String, date: Date): Flow<Result<List<DailyMenu>>>
    
    /**
     * Get weekly menu (7 days from start date)
     */
    fun getWeeklyMenu(userId: String, startDate: Date): Flow<Result<List<DailyMenu>>>
    
    /**
     * Add single meal to a meal type in a specific date
     */
    suspend fun addMealToDailyMenu(
        userId: String,
        date: Date,
        mealId: String,
        mealName: String,
        calories: Double,
        mealType: String,
        protein: Double? = null,
        carbs: Double? = null,
        fat: Double? = null
    ): Result<Long>
    
    /**
     * Add multiple meals to a meal type in a specific date
     */
    suspend fun addMealsToDailyMenu(
        userId: String,
        date: Date,
        meals: List<MealToAdd>
    ): Result<List<Long>>

    /**
     * BỔ SUNG HÀM UPDATE
     * Update the consumed status of a specific meal intake.
     */
    suspend fun updateMealIntake(mealIntakeId: Long, isConsumed: Boolean): Result<Unit>

    /**
     * Delete a specific meal from daily menu
     */
    suspend fun deleteMeal(mealId: Long): Result<Unit>
    
    /**
     * Delete all meals for a specific date
     */
    suspend fun deleteAllMealsByDate(userId: String, date: Date): Result<Unit>
    
    data class MealToAdd(
        val mealId: String,
        val mealName: String,
        val calories: Double,
        val mealType: String,
        val protein: Double? = null,
        val carbs: Double? = null,
        val fat: Double? = null
    )
}

