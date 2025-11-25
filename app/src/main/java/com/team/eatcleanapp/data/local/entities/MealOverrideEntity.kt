package com.team.eatcleanapp.data.local.entities

import androidx.room.Entity

@Entity(
    tableName = "meal_override",
    primaryKeys = ["userId", "mealId"]
)
data class MealOverrideEntity(
    val userId: String,
    val mealId: String,
    val customName: String? = null,
    val customIngredients: String? = null,
    val customInstructions: String? = null,
    val customCalories: Double? = null,
    val customImage: String? = null,
    val updatedAt: Long = System.currentTimeMillis()
)