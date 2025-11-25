package com.team.eatcleanapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

// bang meal_intake
@Entity(tableName = "meal_intake")
data class MealIntakeEntity(
    @PrimaryKey
    val id : String,
    val dailyMenuItemId: String,
    val userId: String,
    val mealId: String,
    val mealName: String,
    val date: Long,
    val mealType: String,
    val calories: Double,
    val portionSize: Double,
    val totalCalories: Double,
    val isConsumed: Boolean = false,
)
