package com.team.eatcleanapp.domain.model

data class User(
    val id: String = "",
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val weight: Double = 0.0,
    val height: Double = 0.0,
    val age: Int = 0,
    val gender: Gender = Gender.MALE,
    val activityMinutesPerDay: Int = 0,
    val activityDaysPerWeek: Int = 0,
    val activityLevel: ActivityLevel = ActivityLevel.SEDENTARY,
    val goal: Goal = Goal.MAINTAIN_WEIGHT,
    val avatarUrl: String? = null,
    val healthMetrics: HealthMetrics? = null
)

enum class ActivityLevel {
    SEDENTARY,
    LIGHTLY_ACTIVE,
    MODERATELY_ACTIVE,
    VERY_ACTIVE,
    EXTRA_ACTIVE
}

enum class Goal {
    LOSE_WEIGHT,
    MAINTAIN_WEIGHT,
    GAIN_WEIGHT
}

enum class MealCategory {
    BREAKFAST,
    LUNCH,
    DINNER
}

enum class Gender {
    MALE,
    FEMALE
}
