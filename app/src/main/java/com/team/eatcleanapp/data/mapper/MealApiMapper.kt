package com.team.eatcleanapp.data.mapper

import com.team.eatcleanapp.data.remote.dto.MealDto
import com.team.eatcleanapp.domain.model.meal.Ingredient
import com.team.eatcleanapp.domain.model.meal.Meal

/**
 * Mapper để chuyển đổi từ MealDto (API) sang Meal (Domain)
 */
object MealApiMapper {
    
    /**
     * Chuyển MealDto từ API sang Meal domain model
     */
    fun MealDto.toDomain(): Meal {
        val rawIngredients = getIngredientsList()
        android.util.Log.d("MealApiMapper", "Raw ingredients from API: ${rawIngredients.size} items")
        rawIngredients.forEach { (name, measure) ->
            android.util.Log.d("MealApiMapper", "  - $name: $measure")
        }
        
        val ingredients = rawIngredients.map { (name, measure) ->
            // Parse measure để lấy quantity và unit
            val (quantity, unit) = parseMeasure(measure)
            
            // Tính calories per 100g từ lookup table
            val caloriesPer100 = IngredientCaloriesLookup.getCaloriesPer100g(name)
            
            // Convert quantity về gram để tính calories
            val quantityInGrams = IngredientCaloriesLookup.convertToGrams(quantity, unit)
            
            // Tính total calories cho ingredient này
            val totalCalories = (caloriesPer100 / 100.0) * quantityInGrams
            
            Ingredient(
                name = name,
                quantity = quantity,
                unit = unit,
                caloriesPer100 = caloriesPer100
            )
        }
        
        val rawInstructions = getInstructionsList()
        android.util.Log.d("MealApiMapper", "Raw instructions from API: ${rawInstructions.size} steps")
        rawInstructions.forEachIndexed { index, step ->
            android.util.Log.d("MealApiMapper", "  Step ${index + 1}: ${step.take(50)}...")
        }
        
        // Tính tổng calories từ tất cả ingredients
        val totalCalories = ingredients.sumOf { ingredient ->
            val quantityInGrams = IngredientCaloriesLookup.convertToGrams(ingredient.quantity, ingredient.unit)
            (ingredient.caloriesPer100 / 100.0) * quantityInGrams
        }
        
        val meal = Meal(
            id = id,
            name = name,
            image = image,
            description = "${category ?: ""}${if (area != null) " - $area" else ""}",
            calories = totalCalories,
            ingredients = ingredients,
            instructions = rawInstructions
        )
        
        android.util.Log.d("MealApiMapper", "Mapped meal: ${meal.name}, ingredients=${meal.ingredients.size}, instructions=${meal.instructions.size}")
        
        return meal
    }
    
    /**
     * Parse measure string thành quantity và unit
     * Ví dụ: "1 cup" -> quantity = 1.0, unit = "cup"
     *        "250g" -> quantity = 250.0, unit = "g"
     */
    private fun parseMeasure(measure: String): Pair<Double, String> {
        if (measure.isBlank()) return Pair(1.0, "piece")
        
        val trimmed = measure.trim()
        
        // Tìm số ở đầu string
        val numberRegex = Regex("""^(\d+\.?\d*)\s*(.*)$""")
        val match = numberRegex.find(trimmed)
        
        return if (match != null) {
            val quantity = match.groupValues[1].toDoubleOrNull() ?: 1.0
            val unit = match.groupValues[2].takeIf { it.isNotBlank() } ?: "piece"
            Pair(quantity, unit)
        } else {
            Pair(1.0, trimmed.ifBlank { "piece" })
        }
    }
    
    /**
     * Chuyển list MealDto sang list Meal
     */
    fun List<MealDto>.toDomain(): List<Meal> {
        return this.mapNotNull { dto ->
            try {
                dto.toDomain()
            } catch (e: Exception) {
                null
            }
        }
    }
}

