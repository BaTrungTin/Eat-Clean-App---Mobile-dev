package com.team.eatcleanapp.data.mapper

import com.google.firebase.firestore.DocumentSnapshot
import com.team.eatcleanapp.data.local.entities.MealEntity
import com.team.eatcleanapp.domain.model.meal.Ingredient
import com.team.eatcleanapp.domain.model.meal.Meal

object MealMapper {

    // Domain -> Entity
    fun Meal.toEntity(): MealEntity {
        return MealEntity(
            id = id,
            name = name,
            description = description,
            calories = if (hasDirectCalories()) calories else totalCalories,
            image = image,
            ingredients = ingredients.joinToString("|") { ingredient ->
                "${ingredient.name},${ingredient.quantity},${ingredient.unit},${ingredient.caloriesPer100}"
            },
            instructions = instructions.joinToString("|")
        )
    }

    // Entity -> Domain
    fun MealEntity.toDomain(): Meal {
        val ingredientList = parseIngredients(ingredients)
        val instructionList = parseInstructions(instructions)

        return Meal(
            id = id,
            name = name,
            image = image,
            description = description,
            calories = calories,
            ingredients = ingredientList,
            instructions = instructionList
        )
    }

    // Firestore -> Domain
    fun DocumentSnapshot.toMeal(): Meal {
        return Meal(
            id = id,
            name = getString("name") ?: "",
            image = getString("image"),
            description = getString("description") ?: "",
            calories = getDouble("calories") ?: 0.0,
            ingredients = parseIngredients(getString("ingredients")),
            instructions = parseInstructions(getString("instructions"))
        )
    }

    // Parse ingredients
    private fun parseIngredients(ingredientsString: String?): List<Ingredient> {
        if (ingredientsString.isNullOrEmpty() || ingredientsString.isBlank()) {
            android.util.Log.d("MealMapper", "parseIngredients: empty or blank string")
            return emptyList()
        }

        val result = ingredientsString.split("|").mapNotNull { ingredientStr ->
            try {
                if (ingredientStr.isBlank()) return@mapNotNull null
                val parts = ingredientStr.split(",")
                if (parts.size == 4) {
                    Ingredient(
                        name = parts[0],
                        quantity = parts[1].toDoubleOrNull() ?: 0.0,
                        unit = parts[2],
                        caloriesPer100 = parts[3].toDoubleOrNull() ?: 0.0
                    )
                } else {
                    android.util.Log.w("MealMapper", "parseIngredients: invalid format - $ingredientStr (parts: ${parts.size})")
                    null
                }
            } catch (e: Exception) {
                android.util.Log.e("MealMapper", "parseIngredients error: ${e.message}", e)
                null
            }
        }
        
        android.util.Log.d("MealMapper", "parseIngredients: parsed ${result.size} ingredients from '$ingredientsString'")
        return result
    }

    // Parse instructions
    private fun parseInstructions(instructionsString: String?): List<String> {
        if (instructionsString.isNullOrEmpty() || instructionsString.isBlank()) {
            android.util.Log.d("MealMapper", "parseInstructions: empty or blank string")
            return emptyList()
        }
        
        val result = instructionsString.split("|")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
        
        android.util.Log.d("MealMapper", "parseInstructions: parsed ${result.size} instructions from '$instructionsString'")
        return result
    }

    // List conversions cho entity -> domain
    fun List<MealEntity>.toDomain(): List<Meal> {
        return this.map { it.toDomain() }
    }

    // List conversions cho domain -> entity
    fun List<Meal>.toEntity(): List<MealEntity> {
        return this.map { it.toEntity() }
    }

    // List conversions cho Firestore documents -> domain
    fun List<DocumentSnapshot>.toMealList(): List<Meal> {
        return this.mapNotNull { document ->
            try {
                document.toMeal()
            } catch (e: Exception) {
                null
            }
        }
    }
    
    // Domain -> Firestore Map
    fun Meal.toFirestoreMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "name" to name,
            "description" to (description ?: ""),
            "calories" to (if (hasDirectCalories()) calories else totalCalories),
            "image" to (image ?: ""),
            "ingredients" to ingredients.joinToString("|") { ingredient ->
                "${ingredient.name},${ingredient.quantity},${ingredient.unit},${ingredient.caloriesPer100}"
            },
            "instructions" to instructions.joinToString("|")
        )
    }
}