package com.team.eatcleanapp.data.mapper

import com.team.eatcleanapp.data.local.entities.MealOverrideEntity
import com.team.eatcleanapp.domain.model.meal.Ingredient
import com.team.eatcleanapp.domain.model.meal.Meal

object MealOverrideMapper {

    // Áp dụng override lên Meal
    fun Meal.applyOverride(override: MealOverrideEntity?): Meal {
        if (override == null) return this
        return copy(
            name = override.customName ?: name,
            calories = override.customCalories ?: calories,
            image = override.customImage ?: image,
            ingredients = parseIngredients(override.customIngredients).ifEmpty { ingredients },
            instructions = parseInstructions(override.customInstructions).ifEmpty { instructions }
        )
    }

    // Tạo MealOverrideEntity từ Meal gốc và Meal đã chỉnh sửa
    fun Meal.toOverrideEntity(originalMeal: Meal, userId: String): MealOverrideEntity {
        return MealOverrideEntity(
            userId = userId,
            mealId = id,
            customName = getCustomValue(
                originalMeal.name, name),
            customCalories = getCustomValue(
                originalMeal.calories, calories),
            customImage = getCustomValue(
                originalMeal.image, image),
            customIngredients = getCustomIngredients(
                originalMeal.ingredients, ingredients),
            customInstructions = getCustomInstructions(
                originalMeal.instructions, instructions),
            updatedAt = System.currentTimeMillis()
        )
    }

    // Chỉ lưu giá trị khi khác với gốc
    private fun <T> getCustomValue(original: T, modified: T): T? {
        return if (original != modified) modified else null
    }

    private fun getCustomIngredients(original: List<Ingredient>, modified: List<Ingredient>): String? {
        return if (original != modified) {
            modified.joinToString("|") { "${it.name},${it.quantity},${it.unit},${it.caloriesPer100}" }
        } else null
    }

    private fun getCustomInstructions(original: List<String>, modified: List<String>): String? {
        return if (original != modified) modified.joinToString("|") else null
    }

    private fun parseIngredients(str: String?): List<Ingredient> {
        return str?.takeIf { it.isNotEmpty() }?.split("|")?.mapNotNull { ingredientStr ->
            val parts = ingredientStr.split(",")
            if (parts.size == 4) {
                Ingredient(
                    name = parts[0],
                    quantity = parts[1].toDoubleOrNull() ?: 0.0,
                    unit = parts[2],
                    caloriesPer100 = parts[3].toDoubleOrNull() ?: 0.0
                )
            } else null
        } ?: emptyList()
    }

    private fun parseInstructions(str: String?): List<String> {
        return str?.takeIf { it.isNotEmpty() }?.split("|") ?: emptyList()
    }

    // Entity -> Domain (Meal)
    fun MealOverrideEntity.toDomain(): Meal {
        return Meal(
            id = mealId,
            name = customName ?: "",
            calories = customCalories ?: 0.0,
            image = customImage,
            ingredients = parseIngredients(customIngredients),
            instructions = parseInstructions(customInstructions)
        )
    }

    // List Entity -> List Domain
    fun List<MealOverrideEntity>.toDomain(): List<Meal> {
        return this.map { it.toDomain() }
    }
}
