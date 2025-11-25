package com.team.eatcleanapp.domain.model.dailymenu

import com.team.eatcleanapp.domain.model.enums.*
import java.util.Date
import java.util.UUID

data class DailyMenuItem(
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val date: Date,
    val mealId: String,
    val mealName: String,
    val calories: Double,
    val mealType: MealCategory,
    val portionSize: Double = 1.0
) {
    val totalCalories: Double
        get() = calories * portionSize
}