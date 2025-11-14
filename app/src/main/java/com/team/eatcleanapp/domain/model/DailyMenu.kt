package com.team.eatcleanapp.domain.model

import java.util.Date

data class DailyMenu(
    val id: String?,
    val userId: String,
    val date: Date,
    val mealId: String,
    val mealName: String,
    val calories: Double,
    val protein: Double? = null,
    val carbs: Double? = null,
    val fat: Double? = null,
    val createdAt: Date? = null,
    val updatedAt: Date? = null
)

