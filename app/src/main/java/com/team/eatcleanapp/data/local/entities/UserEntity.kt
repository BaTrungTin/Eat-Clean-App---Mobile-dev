package com.team.eatcleanapp.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)]
)
data class UserEntity(
    @PrimaryKey
    val id: String,
    val email: String,
    val name: String,
    val weight: Double,
    val height: Double,
    val age: Int,
    val gender: String, // MALE, FEMALE
    val activityMinutesPerDay: Int,
    val activityDaysPerWeek: Int,
    val activityLevel: String,  // SEDENTARY, LIGHTLY_ACTIVE, MODERATELY_ACTIVE, VERY_ACTIVE, EXTRA_ACTIVE
    val goal: String,
    val avatarUrl: String? = null,
    val healthMetrics: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)