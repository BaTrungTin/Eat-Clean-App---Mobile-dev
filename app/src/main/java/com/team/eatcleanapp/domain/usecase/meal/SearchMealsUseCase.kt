package com.team.eatcleanapp.domain.usecase.meal

import com.team.eatcleanapp.domain.model.Meal
import com.team.eatcleanapp.domain.repository.MealRepository


class SearchMealsUseCase(
    private val mealRepository: MealRepository
) {


    suspend operator fun invoke(
        query: String,
        maxCalories: Double? = null
    ): List<Meal> {

        val result = mealRepository.searchMeals(query, maxCalories)

        // Sắp xếp lại theo tên A-Z cho đồng nhất
        return result.sortedBy { it.name.lowercase() }
    }
}