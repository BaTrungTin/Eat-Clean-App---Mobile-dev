package com.team.eatcleanapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index
import com.team.eatcleanapp.util.DateUtils

@Entity(
    tableName = "daily_menu",
    indices = [
        Index(value = ["userId", "date"]),
        Index(value = ["userId", "date", "mealId"])
    ]
)
data class DailyMenuEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val date: String, // Format: "yyyy-MM-dd" (chỉ lưu ngày)
    val mealId: String,
    val mealName: String,
    val calories: Double,
    val quantity: Double,
    val unit: String,
    val category: String // BREAKFAST, LUNCH, DINNER
)