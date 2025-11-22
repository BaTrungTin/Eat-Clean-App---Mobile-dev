package com.team.eatcleanapp.domain.model

data class NutritionInfo(
    val totalCalories : Double,
    val consumedCalories : Double,
) {
    // calo con lai
    val remainingCalories : Double
        get() = totalCalories - consumedCalories

    val isOverLimit: Boolean
        get() = consumedCalories > totalCalories

    val overCalories: Double
        get() = if (isOverLimit) consumedCalories - totalCalories else 0.0

    val remainingNonNegative: Double
        get() = remainingCalories.coerceAtLeast(0.0)

    val progress: Float
        get() = if (totalCalories <= 0) 0f
        else consumedCalories.toFloat() / totalCalories.toFloat()
}
