package com.team.eatcleanapp.data.local.entities

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "daily_menu",
    primaryKeys = ["userId", "date", "mealId", "mealType"],
    indices = [
        Index(value = ["userId", "date"]),
        Index(value = ["userId", "date", "mealId"])
    ]
)
data class DailyMenuEntity(
    val userId: String,
    val date: Long,
    val mealId: String,
    val mealName: String,
    val calories: Double,
    val mealType: String,
    val portionSize: Double = 1.0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

