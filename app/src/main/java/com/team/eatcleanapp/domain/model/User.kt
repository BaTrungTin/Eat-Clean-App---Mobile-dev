package com.team.eatcleanapp.domain.model

data class User(
    val id: String,
    val email: String,
    val password: String,
    val name: String,
    val weight: Double,
    val height: Double,
    val age: Int,
    val gender: Gender,
    val activityMinutesPerDay: Int,
    val activityDaysPerWeek: Int,
    val activityLevel: ActivityLevel,
    val goal: Goal,
    val avatarUrl: String?
)

enum class Gender {
    MALE,
    FEMALE
}

enum class ActivityLevel {
    SEDENTARY,              // ít hoặc không tham gia hoạt động thể chất (1.2)
    LIGHTLY_ACTIVE,         // vận động thể chất, tập thể dục 1 - 3 ngày/tuần (1.375)
    MODERATELY_ACTIVE,      // vận động thể chất, tập thể dục 3 - 5 ngày/tuần (1.55)
    VERY_ACTIVE,            // vận động thể chất, tập thể dục 6 - 7 ngày/tuần (1.725)
    EXTRA_ACTIVE            // Vận động thể chất, tập thể dục hơn 90 phút mỗi ngày (1.9)
}

enum class Goal {
    LOSE_WEIGHT,
    MAINTAIN_WEIGHT,
    GAIN_WEIGHT
}