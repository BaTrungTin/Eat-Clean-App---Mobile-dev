package com.team.eatcleanapp.domain.model

import java.util.Date

data class MealIntake(
    val id: String?,
    val userId: String,
    val mealId: String,
    val mealName: String,
    val calories: Double,
    val quantity: Double = 1.0,
    val unit: String = "portion",
    val protein: Double? = null,
    val carbs: Double? = null,
    val fat: Double? = null,
    val consumedAt: Date,
    val createdAt: Date? = null,
    val updatedAt: Date? = null
)

