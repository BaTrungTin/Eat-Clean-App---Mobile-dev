package com.team.eatcleanapp.domain.model.meal

data class Meal(
    val id: String = "",
    val name: String,
    val image: String? = null,
    val description: String = "",
    val calories: Double = 0.0,
    val ingredients: List<Ingredient> = emptyList(),
    val instructions: List<String> = emptyList()
) {
    // Logic tính toán tổng dinh dưỡng của cả món ăn
    val totalCalories: Double
        get() = ingredients.sumOf { it.totalCalories }

    fun hasDirectCalories(): Boolean = calories > 0
}