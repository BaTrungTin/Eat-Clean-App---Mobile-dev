package com.team.eatcleanapp.domain.model.dailymenu

import java.util.Date
import java.util.UUID


data class DailyMenuItem(
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val date: Date,
    val mealId: String,
    val category: MealCategory,
    val mealName: String,
    val calories: Double,
    val quantity: Double,
    val unit: String
) {
    val totalCalories: Double
        get() = calories * quantity
}

data class DateCategory(
    val date: Date,
    val category: String // "BREAKFAST", "LUNCH", "DINNER"
)

// tạo enum "MealCategory" để tham số mealCategory trong hàm "calculateMealCalories"  hứng giá trị là bữa nào
enum class MealCategory {
    BREAKFAST,
    LUNCH,
    DINNER
}
