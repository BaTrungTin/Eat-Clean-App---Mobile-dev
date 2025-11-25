package com.team.eatcleanapp.domain.model.nutrition

import java.util.Date

data class NutritionInfo(
    val date: Date,
    val targetCalories : Double,    // kcal muc tieu
    val plannedCalories : Double,   // kcal du dinh theo dailymenuday
    val consumedCalories: Double    // kcal da an trong ngay
) {
    // calo con lai
    val remainingCalories : Double
        get() = targetCalories - consumedCalories

    // da vuot muc tieu chua?
    val isOverTarget: Boolean
        get() = consumedCalories > targetCalories

    // da vuot du dinh chua?
    val isOverPlanned: Boolean
        get() = consumedCalories > plannedCalories

    val remainingNonNegative: Double
        get() = remainingCalories.coerceAtLeast(0.0)

    // tien do so voi muc tieu
    val targetProgress: Float
        get() = if (targetCalories <= 0) 0f
        else consumedCalories.toFloat() / targetCalories.toFloat()

    // dua tien do ve %
    fun getTargetProgressPercentage(): Int =
        (targetProgress * 100).toInt()
}