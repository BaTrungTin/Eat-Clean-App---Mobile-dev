package com.team.eatcleanapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals")
data class MealEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val calories: Double,
    val image: String? = null,
    val ingredients: String,
    val instructions: String
)