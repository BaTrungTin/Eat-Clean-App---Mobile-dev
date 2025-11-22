package com.team.eatcleanapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_intake")
data class MealIntakeEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val dailyMenuItemId: String?,
    val date: String,
    val mealId: String,
    val mealName: String,
    val category: String,
    val calories: Double,
    val quantity: Double,
    val unit: String,
    val isChecked: Boolean = false
)