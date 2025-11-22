package com.team.eatcleanapp.data.mapper

import com.team.eatcleanapp.data.remote.dto.IngredientDto
import com.team.eatcleanapp.data.remote.dto.MealDto
import com.team.eatcleanapp.domain.model.Ingredient
import com.team.eatcleanapp.domain.model.Meal

fun MealDto.toMeal(): Meal {
    return Meal(
        id = id,
        name = name,
        calories = calories,
        image = image,
        ingredients = ingredients.map { it.toIngredient() },
        instructions = instructions
    )
}

fun Meal.toMealDto(): MealDto {
    return MealDto(
        id = id,
        name = name,
        calories = calories,
        image = image,
        ingredients = ingredients.map { it.toIngredientDto() },
        instructions = instructions
    )
}

fun IngredientDto.toIngredient(): Ingredient {
    return Ingredient(
        name = name,
        amount = amount
    )
}

fun Ingredient.toIngredientDto(): IngredientDto {
    return IngredientDto(
        name = name,
        amount = amount
    )
}
