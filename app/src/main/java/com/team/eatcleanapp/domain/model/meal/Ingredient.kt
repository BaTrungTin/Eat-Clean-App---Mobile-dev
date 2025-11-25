package com.team.eatcleanapp.domain.model.meal

data class Ingredient(
    val name: String,
    val quantity: Double,     // Số lượng thực tế trong món (ví dụ: 200)
    val unit: String,         // Đơn vị (ví dụ: "g", "ml")
    val caloriesPer100: Double,
) {
    // Logic tính toán dinh dưỡng dựa trên trọng lượng thực tế
    // Giả sử đơn vị chuẩn là gram/ml.
    // Tỉ lệ = số lượng thực tế / 100
    private val ratio: Double
        get() = if (quantity > 0) quantity / 100.0 else 0.0

    val totalCalories: Double get() = caloriesPer100 * ratio
}
