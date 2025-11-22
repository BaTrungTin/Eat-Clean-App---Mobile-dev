package com.team.eatcleanapp.data.local.entities

import androidx.room.Entity
import com.team.eatcleanapp.util.DateUtils

@Entity(
    tableName = "favorites",
    primaryKeys = ["userId", "mealId"]
)
data class FavoriteEntity(
    val userId: String,
    val mealId: String,
    val mealName: String,
    val calories: Double,
    val image: String,
    val ingredients: String,
    val instructions: String,
    val isCustomized: Boolean = false
)


