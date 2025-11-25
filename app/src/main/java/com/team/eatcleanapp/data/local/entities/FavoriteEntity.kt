package com.team.eatcleanapp.data.local.entities

import androidx.room.Entity

@Entity(
    tableName = "favorites",
    primaryKeys = ["userId", "mealId"]
)
data class FavoriteEntity(
    val userId: String,
    val mealId: String,
    val mealName: String,
    val calories: Double,
    val image: String?,
    val createdAt: Long
)

