package com.team.eatcleanapp.domain.model.user

import com.team.eatcleanapp.domain.model.enums.*

data class User(
    val id: String,
    val email: String,
    val name: String,
    val weight: Double = 0.0,
    val height: Double = 0.0,
    val age: Int = 0,
    val gender: Gender = Gender.MALE,
    val activityMinutesPerDay: Int = 0,
    val activityDaysPerWeek: Int = 0,
    val activityLevel: ActivityLevel = ActivityLevel.SEDENTARY,
    val goal: Goal = Goal.MAINTAIN_WEIGHT,
    val avatarUrl: String? = null,
    val healthMetrics: HealthMetrics? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    val activityMinutesPerWeek: Int
        get() = activityMinutesPerDay * activityDaysPerWeek
}