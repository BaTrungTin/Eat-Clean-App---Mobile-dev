package com.team.eatcleanapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "daily_menu",
    indices = [
        Index(value = ["userId", "date"]),
        Index(value = ["userId", "date", "mealId"])
    ]
)
data class DailyMenuEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: String,
    val date: Long, // Timestamp in milliseconds
    val mealId: String,
    val mealName: String,
    val calories: Double,
    val protein: Double? = null,
    val carbs: Double? = null,
    val fat: Double? = null,
    val mealType: String, // BREAKFAST, LUNCH, DINNER, SNACK
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

