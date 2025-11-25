package com.team.eatcleanapp.domain.model

data class Meal(
    val id: String = "",
    val name: String = "",
    val image: String? = null,
    val description: String = "",
    val ingredients: List<Ingredient> = emptyList(),
    val instructions: List<String> = emptyList()
) {
    // Logic tính toán tổng dinh dưỡng của cả món ăn
    val totalCalories: Double
        get() = ingredients.sumOf { it.totalCalories }

    val totalCarbs: Double
        get() = ingredients.sumOf { it.totalCarbs }

    val totalProtein: Double
        get() = ingredients.sumOf { it.totalProtein }

    val totalFat: Double
        get() = ingredients.sumOf { it.totalFat }
}

data class Ingredient(
    val name: String = "",
    val quantity: Double = 0.0,     // Số lượng thực tế trong món (ví dụ: 200)
    val unit: String = "",          // Đơn vị (ví dụ: "g", "ml")
    val caloriesPer100: Double = 0.0,
    val carbsPer100: Double = 0.0,
    val proteinPer100: Double = 0.0,
    val fatPer100: Double = 0.0
) {
    // Logic tính toán dinh dưỡng dựa trên trọng lượng thực tế
    // Giả sử đơn vị chuẩn là gram/ml.
    // Tỉ lệ = số lượng thực tế / 100
    private val ratio: Double
        get() = if (quantity > 0) quantity / 100.0 else 0.0

    val totalCalories: Double get() = caloriesPer100 * ratio
    val totalCarbs: Double get() = carbsPer100 * ratio
    val totalProtein: Double get() = proteinPer100 * ratio
    val totalFat: Double get() = fatPer100 * ratio
}
