package com.team.eatcleanapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "favorites",
    indices = [Index(value = ["userId", "mealId"], unique = true)]
)
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: String,
    val mealId: String,
    val mealName: String,
    val calories: Double,
    val image: String,
    val category: String,
    val createdAt: Long = System.currentTimeMillis()
)

