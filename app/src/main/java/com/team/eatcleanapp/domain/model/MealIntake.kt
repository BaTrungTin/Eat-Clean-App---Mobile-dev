package com.team.eatcleanapp.domain.model

import com.team.eatcleanapp.domain.model.dailymenu.MealCategory
import java.util.Date
import java.util.UUID

data class MealIntake(
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val dailyMenuItemId: String?,
    val date: Date,
    val mealId: String,
    val mealName: String,
    val category: MealCategory,
    val calories: Double,         // Lưu trực tiếp calories
    val quantity: Double,         // Lưu số lượng
    val unit: String,             // Lưu đơn vị
    val isChecked: Boolean = false
)