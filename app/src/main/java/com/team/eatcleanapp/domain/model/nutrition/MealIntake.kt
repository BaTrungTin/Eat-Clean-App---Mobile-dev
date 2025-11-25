package com.team.eatcleanapp.domain.model.nutrition

import com.team.eatcleanapp.domain.model.enums.MealCategory
import java.util.Date
import java.util.UUID

data class MealIntake(
    val id: String = UUID.randomUUID().toString(),
    val dailyMenuItemId: String,
    val userId: String,
    val mealId: String,
    val mealName: String,
    val date: Date,
    val mealType: MealCategory,
    val calories: Double,
    val portionSize: Double,
    val isConsumed: Boolean = false
) {
    val totalCalories: Double
        get() = calories * portionSize

    val consumedCalories: Double
        get() = if (isConsumed) totalCalories
                else 0.0
}