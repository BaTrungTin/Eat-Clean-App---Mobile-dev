package com.team.eatcleanapp.domain.model.dailymenu

data class DailyMenuWeek(
    val days: List<DailyMenuDay>
) {
    val totalWeeklyCalories: Double
        get() = days.sumOf { it.totalCalories }
}
