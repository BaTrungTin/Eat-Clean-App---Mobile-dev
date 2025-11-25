package com.team.eatcleanapp.domain.model.enums

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

enum class MealCategory(val displayName: String, val order: Int) {
    BREAKFAST("Sáng", 0),
    LUNCH("Trưa", 1),
    DINNER("Tối", 2);

    companion object {
        val ALL = entries.toTypedArray()

        fun fromString(value: String): MealCategory? {
            return entries.find { it.name == value }
        }
    }
}

enum class Gender {
    MALE,
    FEMALE
}

enum class RegistrationProgress {
    NEEDS_BASIC_PROFILE,    // Thiếu thông tin cơ bản
    NEEDS_HEALTH_PROFILE,   // Thiếu hồ sơ sức khỏe
    NEEDS_GOAL,            // Thiếu mục tiêu
    COMPLETED              // Đã hoàn thành
}