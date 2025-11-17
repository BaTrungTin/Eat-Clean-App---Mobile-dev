package com.team.eatcleanapp.domain.model

data class NutritionInfo(
    val totalCalories : Int,
    val consumedCalories : Int,
) {
    // calo con lai
    val remainingCalories : Int
        get() = totalCalories - consumedCalories

    val isOverLimit: Boolean
        get() = consumedCalories > totalCalories

    val overCalories: Int
        get() = if (isOverLimit) consumedCalories - totalCalories else 0

    val remainingNonNegative: Int
        get() = remainingCalories.coerceAtLeast(0)

    val progress: Float
        get() = if (totalCalories <= 0) 0f
        else consumedCalories.toFloat() / totalCalories.toFloat()
}
