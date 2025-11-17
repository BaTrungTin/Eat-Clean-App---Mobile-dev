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
    val avatarUrl: String?,
    val healthMetrics: HealthMetrics?
)
// tạo enum  "ActivityLevel" để biến "val activityLevel" ở trên hứng giá trị hoạt động
enum class ActivityLevel {
    SEDENTARY,
    LIGHTLY_ACTIVE,
    MODERATELY_ACTIVE,
    VERY_ACTIVE,
    EXTRA_ACTIVE
}

// tạo enum "Goal" để biến "val goal" ở trên hứng giá trị mục tiêu
enum class Goal {
    LOSE_WEIGHT,
    MAINTAIN_WEIGHT,
    GAIN_WEIGHT
}
// tạo enum "MealCategory" để tham số mealCategory trong hàm "calculateMealCalories"  hứng giá trị là bữa nào
enum class MealCategory {
    BREAKFAST,
    LUNCH,
    DINNER
}

enum class Gender {
    MALE,
    FEMALE
}