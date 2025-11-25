package com.team.eatcleanapp.domain.model.dailymenu

import java.util.Date

data class DailyMenuDay(
    val date: Date,
    val breakfast: List<DailyMenuItem>,
    val lunch: List<DailyMenuItem>,
    val dinner: List<DailyMenuItem>,
) {
    val totalCalories: Double
        get() = breakfast.sumOf { it.totalCalories } +
                lunch.sumOf { it.totalCalories } +
                dinner.sumOf { it.totalCalories }
}